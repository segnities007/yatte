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

### エンティティ（予定）

```kotlin
// タスク
@Entity
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val time: Long, // Epoch Milliseconds
    val minutesBefore: Int,
    val isWeeklyLoop: Boolean,
    val weekDays: String, // List -> JSON String or TypeConverter
    val isCompleted: Boolean,
    val createdAt: Long,
    val alarmTriggeredAt: Long?
)

// 履歴
@Entity
data class HistoryEntity(
    @PrimaryKey val id: String,
    val taskId: String,
    val title: String,
    val completedAt: Long
)
```

---

## Domainモデル詳細

### Task集約

```kotlin
// Entity
data class Task(
    val id: TaskId,
    val title: String,
    val time: LocalTime,
    val minutesBefore: Int,
    val taskType: TaskType,
    val weekDays: List<DayOfWeek>,
    val isCompleted: Boolean,
    val createdAt: LocalDateTime,
    val alarmTriggeredAt: LocalDateTime?
)

// Value Object
@JvmInline
value class TaskId(val value: String)

enum class TaskType { ONE_TIME, WEEKLY_LOOP }

// UseCase
class CreateTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<TaskId>
}

class GetTodayTasksUseCase(private val repo: TaskRepository) {
    operator fun invoke(): Flow<List<Task>>
}

class CompleteTaskUseCase(
    private val taskRepo: TaskRepository,
    private val historyRepo: HistoryRepository
) {
    suspend operator fun invoke(taskId: TaskId): Result<Unit>
}
```

### History集約

```kotlin
data class History(
    val id: HistoryId,
    val taskId: TaskId,
    val title: String,
    val completedAt: LocalDateTime
)

@JvmInline
value class HistoryId(val value: String)

class GetHistoryTimelineUseCase(private val repo: HistoryRepository) {
    operator fun invoke(date: LocalDate): Flow<List<History>>
}
```

### Settings集約

```kotlin
data class UserSettings(
    val defaultMinutesBefore: Int,
    val notificationSound: Boolean,
    val notificationVibration: Boolean,
    val themeMode: ThemeMode
)

enum class ThemeMode { LIGHT, DARK, SYSTEM }
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

## 依存関係（予定）

```kotlin
// libs.versions.toml
[versions]
kotlin = "2.0.0"
compose = "1.6.0"
room = "2.7.0"
koin = "3.5.0"

[libraries]
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }
```

---

*作成日: 2025-12-20*
