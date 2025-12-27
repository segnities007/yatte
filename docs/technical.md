# 技術仕様

## 技術スタック

| 項目 | 技術 | 備考 |
|------|------|------|
| フレームワーク | **Compose Multiplatform** | Kotlin製のクロスプラットフォームUI |
| 言語 | **Kotlin** | 2.0以上推奨 |
| DBライブラリ | **Room** | KMP対応版（2.7.0+） |
| DI | **Koin** | シンプル、KMP対応 |
| アーキテクチャ | **MVI** | Model-View-Intent |

---

## 対応プラットフォーム

| プラットフォーム | 対応 | 備考 |
|----------------|------|------|
| Android | ✅ | メインターゲット |
| iOS | ✅ | Compose Multiplatform |
| Desktop | ✅ | Windows / macOS / Linux |
| Web | ❌ | 対象外 |

---

## アーキテクチャ

### 全体アーキテクチャ: Layered Architecture（Clean Architecture風）

```
┌─────────────────────────────────────────────────────────────────┐
│                        Presentation                             │
│  ・Compose UI / Screen / ViewModel                              │
│  ・MVIパターンでUI状態管理                                        │
└──────────────────────────┬──────────────────────────────────────┘
                           │ UseCaseを呼び出す
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                          Domain                                 │
│  ・UseCase（ビジネスロジック）                                    │
│  ・Model（ドメインモデル/エンティティ/値オブジェクト）               │
│  ・Repository Interface（抽象）                                  │
└──────────────────────────┬──────────────────────────────────────┘
                           │ Repositoryインターフェースを実装
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                           Data                                  │
│  ・Repository Implementation                                    │
│  ・Local Data Source（Room DAO / DataStore）                    │
│  ・Mapper（Entity ↔ Domain Model変換）                          │
└─────────────────────────────────────────────────────────────────┘
```

### 依存ルール

| ルール | 説明 |
|--------|------|
| **内向き依存** | 外側のレイヤーは内側に依存。逆は禁止。 |
| **Domain独立** | Domainは他レイヤーに依存しない（純粋なKotlin） |
| **DIP** | DataはDomainのRepositoryインターフェースを実装 |

```
Presentation → Domain ← Data
              (抽象に依存)
```

---

### Presentation層: MVI（Model-View-Intent）

```
┌─────────────────────────────────────┐
│               View                  │
│  (Compose UI)                       │
└──────────────┬──────────────────────┘
               │ Intent（ユーザーアクション）
               ▼
┌─────────────────────────────────────┐
│            ViewModel                │
│  - Intentを受け取る                  │
│  - UseCaseを呼び出す                 │
│  - Reducerで状態更新                 │
│  - 新しいStateを発行                 │
└──────────────┬──────────────────────┘
               │ State（UIの状態）
               ▼
┌─────────────────────────────────────┐
│               View                  │
│  (Stateを元に描画)                   │
└─────────────────────────────────────┘
```

---

### Domain層: DDD風の集約

| 要素 | 役割 |
|------|------|
| **Model** | ドメインモデル（Entity / Value Object） |
| **UseCase** | ビジネスロジック（1機能1UseCase） |
| **Repository Interface** | データアクセスの抽象化 |

---

### Data層: Repository Pattern

```
┌─────────────────────────────────────────────────────────────────┐
│                    RepositoryImpl                               │
│  ・Domainの Repository Interface を実装                          │
│  ・データソースの選択・調整を担当                                  │
└──────────────────────────┬──────────────────────────────────────┘
                           │
               ┌───────────┴───────────┐
               ▼                       ▼
┌──────────────────────────┐ ┌──────────────────────────┐
│   LocalDataSource        │ │   (RemoteDataSource)     │
│   ・Room DAO             │ │   ・今回は対象外          │
│   ・DataStore            │ │                          │
└──────────────────────────┘ └──────────────────────────┘
```

| コンポーネント | 役割 |
|---------------|------|
| **RepositoryImpl** | Domainインターフェースの実装、データソース調整 |
| **LocalDataSource** | Room DAO / DataStoreによるローカル永続化 |
| **Mapper** | Entity（DB）↔ Model（Domain）の変換 |
| **Entity** | DBテーブルに対応するデータクラス |

### レイヤー構成（Layer-First → Feature/Aggregate分割）

**横（レイヤー）で切り**、**縦はPresentation=Feature、Domain/Data=Aggregate**で切る。

```
root/
├── :app

├── :presentation/                       # Feature（画面）で分割
│   ├── :presentation:common/
│   │   ├── components/TaskCard.kt, BottomSheet.kt
│   │   └── theme/Theme.kt, Color.kt
│   ├── :presentation:feature:home/
│   │   └── HomeScreen.kt, ViewModel, State
│   ├── :presentation:feature:task/
│   │   ├── list/TaskListScreen.kt, ViewModel
│   │   └── create/CreateTaskScreen.kt, ViewModel
│   ├── :presentation:feature:history/
│   └── :presentation:feature:settings/

├── :domain/                             # Aggregate（集約）で分割
│   ├── :domain:common/
│   ├── :domain:aggregate:task/
│   │   ├── model/Task.kt, TaskId.kt, WeekDay.kt
│   │   ├── repository/TaskRepository.kt
│   │   └── usecase/CreateTask, GetTasks
│   ├── :domain:aggregate:history/
│   ├── :domain:aggregate:alarm/
│   └── :domain:aggregate:settings/

├── :data/                               # Aggregateに対応
│   ├── :data:common/
│   │   └── database/AppDatabase.kt
│   ├── :data:aggregate:task/
│   │   ├── local/TaskDao.kt, TaskEntity.kt
│   │   └── repository/TaskRepositoryImpl.kt
│   ├── :data:aggregate:history/
│   ├── :data:aggregate:alarm/
│   │   └── platform/Android, Ios, Desktop
│   └── :data:aggregate:settings/

└── :di/
```

---

### 依存関係

```
:presentation:feature:* → :domain:aggregate:* ← :data:aggregate:*
```

### FeatureとAggregateの対応

```
:presentation:feature:home/ → :domain:aggregate:task/, history/
:presentation:feature:task/ → :domain:aggregate:task/
```

---

### ポイント

| レイヤー | 分割基準 | 命名 |
|---------|---------|------|
| **Presentation** | Feature（画面） | `:presentation:feature:XXX` |
| **Domain** | Aggregate（集約） | `:domain:aggregate:XXX` |
| **Data** | Aggregate（集約） | `:data:aggregate:XXX` |

---

### メリット

- **Presentation**: 画面単位で分かりやすい
- **Domain/Data**: データ整合性単位で正しく分割
- **スケーラブル**: Feature追加、Aggregate追加が独立

---

## データ永続化

### Room（KMP対応版）

| 項目 | 内容 |
|------|------|
| バージョン | 2.7.0+ |
| KMP対応 | ✅ Android / iOS / Desktop |
| DAO | suspend関数で定義 |

### エンティティ

```kotlin
// タスク
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    @ColumnInfo(name = "alarm_time") val alarmTime: Long,
    @ColumnInfo(name = "minutes_before") val minutesBefore: Int,
    @ColumnInfo(name = "task_type") val taskType: String,
    @ColumnInfo(name = "week_days") val weekDays: String,
    @ColumnInfo(name = "completed_dates") val completedDates: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "alarm_triggered_at") val alarmTriggeredAt: Long?,
    @ColumnInfo(name = "skip_until") val skipUntil: Long?
)

// 履歴
@Entity(tableName = "histories")
data class HistoryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "task_id") val taskId: String,
    val title: String,
    @ColumnInfo(name = "completed_at") val completedAt: Long,
    val status: String
)

// アラーム
@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "task_id") val taskId: String,
    @ColumnInfo(name = "scheduled_at") val scheduledAt: Long,
    @ColumnInfo(name = "notify_at") val notifyAt: Long,
    @ColumnInfo(name = "is_triggered") val isTriggered: Boolean
)
```

---

## Domainモデル詳細

### Task集約

```kotlin
// ドメインモデル
data class Task(
    val id: TaskId,
    val title: String,
    val time: LocalTime,
    val minutesBefore: Int = 10,
    val taskType: TaskType = TaskType.ONE_TIME,
    val weekDays: List<DayOfWeek> = emptyList(),
    val completedDates: Set<LocalDate> = emptySet(),
    val createdAt: LocalDateTime,
    val alarmTriggeredAt: LocalDateTime? = null,
    val skipUntil: LocalDate? = null
) {
    // バリデーション
    init {
        require(title.isNotBlank()) { "タイトルは必須です" }
        require(title.length <= 100) { "タイトルは100文字以下" }
        require(minutesBefore in 0..60) { "通知時間は0〜60分" }
        if (taskType == TaskType.WEEKLY_LOOP) {
            require(weekDays.isNotEmpty()) { "週次タスクには曜日必須" }
        }
    }
    
    // メソッド
    fun complete(date: LocalDate): Task
    fun resetCompletion(date: LocalDate): Task
    fun isCompletedOn(date: LocalDate): Boolean
    fun isActiveOn(date: LocalDate): Boolean
    fun skip(until: LocalDate): Task
    fun cancelSkip(): Task
    fun shouldBeDeleted(now: LocalDateTime, timeZone: TimeZone): Boolean
}

// Value Object
@JvmInline
value class TaskId(val value: String) {
    companion object {
        fun generate(): TaskId = TaskId(Uuid.random().toString())
    }
}

enum class TaskType { ONE_TIME, WEEKLY_LOOP }

// UseCase
class CreateTaskUseCase(repo: TaskRepository)
class UpdateTaskUseCase(repo: TaskRepository)
class DeleteTaskUseCase(repo: TaskRepository)
class CompleteTaskUseCase(taskRepo: TaskRepository, historyRepo: HistoryRepository)
class SkipTaskUseCase(repo: TaskRepository)
class GetTodayTasksUseCase(repo: TaskRepository)
class GetAllTasksUseCase(repo: TaskRepository)
class GetTaskByIdUseCase(repo: TaskRepository)
```

### History集約

```kotlin
data class History(
    val id: HistoryId,
    val taskId: TaskId,
    val title: String,
    val completedAt: LocalDateTime,
    val status: HistoryStatus = HistoryStatus.COMPLETED
) {
    init {
        require(title.isNotBlank()) { "タイトルは必須です" }
    }
}

@JvmInline
value class HistoryId(val value: String) {
    companion object {
        fun generate(): HistoryId = HistoryId(Uuid.random().toString())
    }
}

enum class HistoryStatus {
    COMPLETED,  // ユーザーが完了マーク
    SKIPPED,    // スキップ
    EXPIRED     // 24時間経過で自動削除
}

// UseCase
class AddHistoryUseCase(repo: HistoryRepository)
class DeleteHistoryUseCase(repo: HistoryRepository)
class ClearAllHistoryUseCase(repo: HistoryRepository)
class GetHistoryTimelineUseCase(repo: HistoryRepository)
class ExportHistoryUseCase(repo: HistoryRepository)  // CSV/JSON出力
```

### Settings集約

```kotlin
data class UserSettings(
    val defaultMinutesBefore: Int = 10,
    val notificationSound: Boolean = true,
    val notificationVibration: Boolean = true,
    val customSoundUri: String? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
) {
    init {
        require(defaultMinutesBefore in 0..60)
    }
    
    companion object {
        val DEFAULT = UserSettings()
    }
}

enum class ThemeMode { LIGHT, DARK, SYSTEM }

// UseCase
class GetSettingsUseCase(repo: SettingsRepository)
class UpdateSettingsUseCase(repo: SettingsRepository)
class ResetAllDataUseCase(taskRepo: TaskRepository, historyRepo: HistoryRepository, alarmRepo: AlarmRepository)
```

### Alarm集約

```kotlin
data class Alarm(
    val id: AlarmId,
    val taskId: TaskId,
    val scheduledAt: LocalDateTime,
    val notifyAt: LocalDateTime,
    val isTriggered: Boolean = false
) {
    init {
        require(notifyAt <= scheduledAt) { "通知時刻は予定時刻より前" }
    }
    
    fun markAsTriggered(): Alarm
}

@JvmInline
value class AlarmId(val value: String) {
    companion object {
        fun generate(): AlarmId = AlarmId(Uuid.random().toString())
    }
}

// UseCase
class ScheduleAlarmUseCase(repo: AlarmRepository, scheduler: AlarmScheduler)
class CancelAlarmUseCase(repo: AlarmRepository, scheduler: AlarmScheduler)
class GetScheduledAlarmsUseCase(repo: AlarmRepository)

// Platform Interface
interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarmId: AlarmId)
}
```

---

## MVI状態管理

### 構成

```
User Action → Intent → ViewModel → Reducer → State → UI
                          ↓
                       UseCase
```

### Task画面の例

```kotlin
// State
data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Intent
sealed interface TaskListIntent {
    data object LoadTasks : TaskListIntent
    data class CompleteTask(val taskId: TaskId) : TaskListIntent
    data class DeleteTask(val taskId: TaskId) : TaskListIntent
}

// ViewModel
class TaskListViewModel(
    private val getTodayTasks: GetTodayTasksUseCase,
    private val completeTask: CompleteTaskUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state.asStateFlow()
    
    fun onIntent(intent: TaskListIntent) {
        when (intent) {
            is TaskListIntent.LoadTasks -> loadTasks()
            is TaskListIntent.CompleteTask -> complete(intent.taskId)
            is TaskListIntent.DeleteTask -> delete(intent.taskId)
        }
    }
    
    private fun loadTasks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getTodayTasks().collect { tasks ->
                _state.update { it.copy(tasks = tasks, isLoading = false) }
            }
        }
    }
}
```

### エラーハンドリング

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

// UseCase内で使用
suspend fun createTask(task: Task): Result<TaskId> = runCatching {
    repo.insert(task)
    Result.Success(task.id)
}.getOrElse { Result.Error(it) }
```

---

## 通知・アラーム

### プラットフォーム別実装

| プラットフォーム | 技術 |
|----------------|------|
| Android | AlarmManager / WorkManager |
| iOS | UNUserNotificationCenter |
| Desktop | Coroutines + システム通知 |

### 要件

- バックグラウンドでも動作
- デバイス再起動後も復元
- カスタム音源対応

---


