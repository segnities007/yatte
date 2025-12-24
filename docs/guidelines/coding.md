# コーディング規約

## 目次

1. [ID の扱い方](#id-の扱い方)
2. [Null安全性](#null安全性)
3. [不変性（Immutability）](#不変性immutability)
4. [エラーハンドリング](#エラーハンドリング)
5. [命名規則](#命名規則)
6. [関数設計](#関数設計)
7. [Coroutines と Flow](#coroutines-と-flow)
8. [Clean Architecture 固有](#clean-architecture-固有)

---

## ID の扱い方

### Value Class を使用する

ID系のフィールドは必ず **value class** を使用し、プリミティブ型（`String`, `Int`）を直接使用しないこと。

```kotlin
// ✅ 正しい
@JvmInline
value class TaskId(val value: String)

data class Task(
    val id: TaskId,  // value class を使用
    val title: String,
)

// ❌ 間違い
data class Task(
    val id: String,  // プリミティブ型は使用しない
    val title: String,
)
```

### value class vs typealias

| | `value class` | `typealias` |
|---|---|---|
| **型安全性** | ✅ コンパイル時に別型として扱われる | ❌ 同じ型として扱われる |
| **ランタイムコスト** | なし（インライン化） | なし |
| **推奨度** | ✅ 使用すべき | ❌ ID には使用禁止 |

### ID の命名規則

- 集約ルートのIDは `[集約名]Id` とする（例: `TaskId`, `HistoryId`）
- `@JvmInline` アノテーションを付ける
- `companion object` に `generate()` メソッドを実装

---

## Null安全性

### nullable を避ける

可能な限り non-null 型を使用する。

```kotlin
// ✅ デフォルト値を使用
data class Task(
    val title: String = "",
    val description: String = "",
)

// ❌ nullable は最後の手段
data class Task(
    val title: String?,
    val description: String?,
)
```

### nullable が必要な場合

「値がない」ことに意味がある場合のみ nullable を使用。

```kotlin
data class Task(
    val alarmTriggeredAt: LocalDateTime? = null,  // ✅ null = 未発火という意味がある
)
```

### Safe Call と Elvis

```kotlin
// ✅ safe call + elvis
val name = user?.name ?: "Unknown"

// ❌ !! は絶対禁止（テストコード以外）
val name = user!!.name
```

---

## 不変性（Immutability）

### val を使用する

```kotlin
// ✅ 常に val
val items = mutableListOf<Item>()

// ❌ var は避ける
var items = listOf<Item>()
```

### data class は不変に

```kotlin
// ✅ 状態変更は copy() で新しいインスタンスを返す
data class Task(val isCompleted: Boolean = false) {
    fun complete(): Task = copy(isCompleted = true)
}

// ❌ var プロパティは使わない
data class Task(var isCompleted: Boolean = false)
```

### コレクションは不変型を公開

```kotlin
// ✅ 外部には List (不変) を公開
class TaskRepository {
    private val _tasks = mutableListOf<Task>()
    val tasks: List<Task> get() = _tasks.toList()
}
```

---

## エラーハンドリング

### Result を使用する

UseCase は `Result<T>` を返す。

```kotlin
// ✅ Result で成功/失敗を表現
class CreateTaskUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<TaskId> = runCatching {
        repo.insert(task)
        task.id
    }
}
```

### 例外は投げない

Domain 層では例外を投げず、Result で表現する。

```kotlin
// ✅ Result.failure
suspend fun getTask(id: TaskId): Result<Task> = runCatching {
    repo.getById(id) ?: throw IllegalArgumentException("Task not found")
}

// 呼び出し側
getTask(id)
    .onSuccess { task -> /* 処理 */ }
    .onFailure { error -> /* エラー処理 */ }
```

---

## 命名規則

### パッケージ

```
com.segnities007.yatte.[レイヤー].[集約].[種類]
```

例:
- `com.segnities007.yatte.domain.aggregate.task.model`
- `com.segnities007.yatte.domain.aggregate.task.usecase`
- `com.segnities007.yatte.data.aggregate.task.repository`

### クラス/インターフェース

| 種類 | 命名規則 | 例 |
|-----|---------|---|
| Entity/Model | 単数形の名詞 | `Task`, `History` |
| Value Object | `[名詞]Id`, `[名詞]Type` | `TaskId`, `TaskType` |
| Repository | `[Entity]Repository` | `TaskRepository` |
| UseCase | `[動詞][名詞]UseCase` | `CreateTaskUseCase` |
| ViewModel | `[Feature]ViewModel` | `HomeViewModel` |

### 関数

| 種類 | 命名規則 | 例 |
|-----|---------|---|
| 取得 | `get[名詞]`, `find[名詞]` | `getById`, `findAll` |
| 作成 | `create[名詞]`, `insert` | `createTask`, `insert` |
| 更新 | `update[名詞]` | `updateTask` |
| 削除 | `delete[名詞]`, `remove` | `deleteTask` |
| 変換 | `to[型]`, `as[型]` | `toEntity`, `asDomain` |

---

## 関数設計

### 単一責任

1つの関数は1つのことだけを行う。

```kotlin
// ✅ 単一責任
suspend fun completeTask(id: TaskId): Result<Unit>
suspend fun addHistory(history: History): Result<Unit>

// ❌ 複数の責任
suspend fun completeTaskAndAddHistory(id: TaskId): Result<Unit>
```

### 引数は3つまで

4つ以上の場合は data class にまとめる。

```kotlin
// ✅ data class でまとめる
data class CreateTaskParams(
    val title: String,
    val time: LocalTime,
    val taskType: TaskType,
    val weekDays: List<DayOfWeek>,
)

fun createTask(params: CreateTaskParams): Task

// ❌ 引数が多すぎる
fun createTask(title: String, time: LocalTime, type: TaskType, days: List<DayOfWeek>): Task
```

### operator invoke

UseCase は `operator fun invoke()` を使用。

```kotlin
class GetTodayTasksUseCase(private val repo: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repo.getTodayTasks()
}

// 呼び出し
val tasks = getTodayTasksUseCase()  // invoke を省略可能
```

---

## Coroutines と Flow

### suspend vs Flow

| 用途 | 使用する型 |
|-----|----------|
| 単発の非同期処理 | `suspend fun` |
| リアクティブなデータストリーム | `Flow<T>` |

```kotlin
// ✅ 単発 = suspend
suspend fun insert(task: Task)
suspend fun getById(id: TaskId): Task?

// ✅ ストリーム = Flow
fun getTodayTasks(): Flow<List<Task>>
fun getSettings(): Flow<UserSettings>
```

### Flow の収集

ViewModel では `stateIn` で StateFlow に変換。

```kotlin
class HomeViewModel(
    private val getTodayTasksUseCase: GetTodayTasksUseCase
) : ViewModel() {
    val tasks: StateFlow<List<Task>> = getTodayTasksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
```

---

## Clean Architecture 固有

### 依存の方向

```
Presentation → Domain ← Data
```

- Domain は他のレイヤーに依存しない（純粋Kotlin）
- Presentation と Data は Domain に依存する

### レイヤー間のデータ変換

```kotlin
// Data層: Entity → Domain Model
fun TaskEntity.toDomain(): Task = Task(
    id = TaskId(this.id),
    title = this.title,
    // ...
)

// Data層: Domain Model → Entity
fun Task.toEntity(): TaskEntity = TaskEntity(
    id = this.id.value,
    title = this.title,
    // ...
)
```

### Repository の実装場所

- **インターフェース**: `domain/aggregate/[name]/repository/`
- **実装**: `data/aggregate/[name]/repository/`

```kotlin
// domain層
interface TaskRepository {
    fun getTodayTasks(): Flow<List<Task>>
}

// data層
class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {
    override fun getTodayTasks(): Flow<List<Task>> =
        dao.getTodayTasks().map { entities ->
            entities.map { it.toDomain() }
        }
}
```

