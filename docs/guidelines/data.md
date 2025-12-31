# Data層ガイドライン

## 目次

1. [概要](#概要)
2. [ディレクトリ構造](#ディレクトリ構造)
3. [Entity（エンティティ）](#entityエンティティ)
4. [DAO（データアクセスオブジェクト）](#daoデータアクセスオブジェクト)
5. [Mapper（マッパー）](#mapperマッパー)
6. [RepositoryImpl（リポジトリ実装）](#repositoryimplリポジトリ実装)
7. [AppDatabase](#appdatabase)
8. [型変換ルール](#型変換ルール)
9. [依存関係](#依存関係)

---

## 概要

Data層はDomain層で定義されたRepository Interfaceを実装し、データの永続化を担当する。

```
Domain (Repository Interface)
         ↑ 実装
Data (RepositoryImpl → DAO → Entity)
```

### 技術スタック

| 技術 | 用途 |
|-----|------|
| **Room 2.7.0+** | KMP対応ローカルDB |
| **DataStore** | 設定の永続化（Settings用） |

### 原則

| 原則 | 説明 |
|-----|------|
| **SSOT** | Roomデータベースが信頼できる唯一の情報源 |
| **Entity ≠ Model** | DBスキーマとドメインモデルは分離 |
| **非同期** | DAOは `suspend` / `Flow` を使用 |

---

## ディレクトリ構造

```
data/
├── core/                           # 共通要素
│   ├── database/AppDatabase.kt     # Room Database定義
│   └── converter/TypeConverters.kt # 型変換
├── aggregate/
│   └── [集約名]/
│       ├── local/
│       │   ├── [Name]Entity.kt     # Roomエンティティ
│       │   └── [Name]Dao.kt        # DAO
│       ├── mapper/
│       │   └── [Name]Mapper.kt     # Entity ↔ Model変換
│       └── repository/
│           └── [Name]RepositoryImpl.kt
```

---

## Entity（エンティティ）

### 基本構造

```kotlin
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    
    val title: String,
    
    @ColumnInfo(name = "alarm_time")
    val alarmTime: Long,  // Epoch milliseconds
    
    @ColumnInfo(name = "minutes_before")
    val minutesBefore: Int,
    
    @ColumnInfo(name = "task_type")
    val taskType: String,  // "ONE_TIME" or "WEEKLY_LOOP"
    
    @ColumnInfo(name = "week_days")
    val weekDays: String,  // JSON: ["MONDAY","WEDNESDAY"]
    
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "alarm_triggered_at")
    val alarmTriggeredAt: Long?,
    
    @ColumnInfo(name = "skip_until")
    val skipUntil: Long?,  // Epoch days
)
```

### 命名規則

| 項目 | 規則 | 例 |
|-----|------|-----|
| クラス名 | `[Name]Entity` | `TaskEntity` |
| テーブル名 | スネークケース複数形 | `tasks`, `histories` |
| カラム名 | スネークケース | `created_at`, `is_completed` |

### 厳格な実装ルール

| 項目 | ルール | 理由 |
|------|------|------|
| **PrimaryKey** | **必ず String (UUID) を使用する** | オフライン分散、マージ競合の回避。<br>❌ `Int/Long` の AutoIncrement は禁止。 |
| **List/Object** | **JSON String に変換して保存する** | 正規化のしすぎを防ぐ。<br>❌ 単純なデータ構造のために別テーブルを作らない。 |
| **Enum** | **String として保存する** | 順序変更に強くする (`name` プロパティを使用)。 |
| **日時** | **Long (Epoch Millis) で保存する** | タイムゾーン問題を回避し、ソートを高速化。 |

---

## DAO（データアクセスオブジェクト）

### 基本構造

```kotlin
@Dao
interface TaskDao {
    // 取得（リアクティブ）
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): TaskEntity?
    
    // 挿入
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TaskEntity)
    
    // 更新
    @Update
    suspend fun update(entity: TaskEntity)
    
    // 削除
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}
```

### 使い分け

| 操作 | 戻り値 |
|-----|--------|
| **リアクティブ取得** | `Flow<List<T>>` / `Flow<T?>` |
| **単発取得** | `suspend fun`: `T?` |
| **挿入/更新/削除** | `suspend fun` |

### クエリルール

```kotlin
// ✅ 良い例: パラメータはプリミティブ型
@Query("SELECT * FROM tasks WHERE id = :id")
suspend fun getById(id: String): TaskEntity?

// ❌ 禁止: value class をそのまま渡す
// Roomはvalue classを正しく認識できない場合があるため、必ずプリミティブ型(String)に展開して渡すこと。
@Query("SELECT * FROM tasks WHERE id = :id")
suspend fun getById(id: TaskId): TaskEntity?
```

---

## Mapper（マッパー）

### 基本構造

```kotlin
// Entity → Domain Model
fun TaskEntity.toDomain(): Task {
    return Task(
        id = TaskId(id),
        title = title,
        time = Instant.fromEpochMilliseconds(alarmTime)
            .toLocalDateTime(TimeZone.currentSystemDefault()).time,
        minutesBefore = minutesBefore,
        taskType = TaskType.valueOf(taskType),
        weekDays = parseWeekDays(weekDays),
        isCompleted = isCompleted,
        createdAt = Instant.fromEpochMilliseconds(createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        alarmTriggeredAt = alarmTriggeredAt?.let {
            Instant.fromEpochMilliseconds(it)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        },
        skipUntil = skipUntil?.let {
            LocalDate.fromEpochDays(it.toInt())
        },
    )
}

// Domain Model → Entity
fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id.value,
        title = title,
        alarmTime = createdAt.date.atTime(time)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        minutesBefore = minutesBefore,
        taskType = taskType.name,
        weekDays = weekDays.toJsonString(),
        isCompleted = isCompleted,
        createdAt = createdAt.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds(),
        alarmTriggeredAt = alarmTriggeredAt?.toInstant(TimeZone.currentSystemDefault())
            ?.toEpochMilliseconds(),
        skipUntil = skipUntil?.toEpochDays()?.toLong(),
    )
}
```

### ヘルパー関数

```kotlin
// List<DayOfWeek> → JSON String
private fun List<DayOfWeek>.toJsonString(): String =
    joinToString(",") { it.name }

// JSON String → List<DayOfWeek>
private fun parseWeekDays(json: String): List<DayOfWeek> =
    if (json.isBlank()) emptyList()
    else json.split(",").map { DayOfWeek.valueOf(it) }
```

---

## RepositoryImpl（リポジトリ実装）

### 基本構造

```kotlin
class TaskRepositoryImpl(
    private val dao: TaskDao,
) : TaskRepository {
    
    override fun getTodayTasks(): Flow<List<Task>> =
        dao.getAll().map { entities ->
            entities.map { it.toDomain() }
                .filter { it.isActiveOn(Clock.System.todayIn(TimeZone.currentSystemDefault())) }
        }
    
    override fun getAllTasks(): Flow<List<Task>> =
        dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    
    override suspend fun getById(id: TaskId): Task? =
        dao.getById(id.value)?.toDomain()
    
    override suspend fun insert(task: Task) =
        dao.insert(task.toEntity())
    
    override suspend fun update(task: Task) =
        dao.update(task.toEntity())
    
    override suspend fun delete(id: TaskId) =
        dao.deleteById(id.value)
    
    override suspend fun deleteExpiredTasks() {
        // 期限切れタスクの削除ロジック
    }
}
```

### ルール

| ルール | 説明 |
|--------|------|
| Domain Repository を実装 | `class TaskRepositoryImpl : TaskRepository` |
| DAO を注入 | コンストラクタで受け取る |
| Mapper を使用 | Entity ↔ Model 変換 |
| value class は展開 | `id.value` で String に |

---

## AppDatabase

### 基本構造

```kotlin
@Database(
    entities = [
        TaskEntity::class,
        HistoryEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun historyDao(): HistoryDao
    
    companion object {
        const val DATABASE_NAME = "yatte.db"
    }
}
```

### KMP対応

```kotlin
// commonMain
expect fun createDatabase(context: Any?): AppDatabase

// androidMain
actual fun createDatabase(context: Any?): AppDatabase {
    val appContext = context as Context
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()
}

// iosMain / jvmMain
actual fun createDatabase(context: Any?): AppDatabase {
    // 各プラットフォーム固有の実装
}
```

---

## 型変換ルール

| Domain型 | Entity型 | 変換 |
|---------|---------|------|
| `TaskId` | `String` | `id.value` / `TaskId(id)` |
| `LocalDateTime` | `Long` | Epoch milliseconds |
| `LocalDate` | `Long` | Epoch days |
| `LocalTime` | `Long` | Epoch millis of day |
| `TaskType` | `String` | `.name` / `valueOf()` |
| `List<DayOfWeek>` | `String` | JSON文字列 |
| `ThemeMode` | `String` | `.name` / `valueOf()` |

---

## 依存関係

### モジュール依存

```kotlin
// data:core
dependencies {
    implementation(projects.domain.core)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}

// data:aggregate:task
dependencies {
    implementation(projects.data.core)
    implementation(projects.domain.aggregate.task)
}
```

### 循環依存の回避

```
data:core → domain:core         ✅
data:aggregate:task → domain:aggregate:task  ✅
data:aggregate:task → data:core  ✅
```

---

## チェックリスト

新しいData実装を追加する際：

- [ ] Entity は `@Entity` アノテーション付き
- [ ] カラム名はスネークケース
- [ ] DAO は `interface` で定義
- [ ] Flow 戻り値のメソッドは suspend 不要
- [ ] Mapper は拡張関数として実装
- [ ] RepositoryImpl は Domain Interface を実装
- [ ] value class は展開して使用
- [ ] AppDatabase に Entity と DAO を登録

---

*作成日: 2025-12-24*
