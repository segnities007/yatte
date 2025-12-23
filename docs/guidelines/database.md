# データベース実装ガイドライン

yatteでは**Room (KMP対応版)**を使用し、ローカルデータを永続化します。

## 🏛️ データ設計原則

- **Source of Truth**: Roomデータベースを信頼できる唯一の情報源（SSOT）とする。
- **Entity ≠ Domain Model**: DBのテーブル構造（Entity）とビジネスロジックのデータ（Model）は分離し、Mapperで変換する。
- **非同期処理**: DAOのメソッドは必ず `suspend` 関数または `Flow` を返す。

---

## 🛠️ 実装ステップ

### 1. Entity定義 (`:data`)

DBテーブル定義。Framework（Room）に依存して良い。

```kotlin
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    val isCompleted: Boolean
)
```

### 2. DAO定義 (`:data`)

データアクセスオブジェクト。`interface`として定義する。

```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>> // Flowは自動更新される

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: String)
}
```

### 3. Mapper実装

EntityとModelの相互変換を行う拡張関数を定義。

```kotlin
fun TaskEntity.toDomain(): Task {
    return Task(
        id = TaskId(id),
        title = title,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        isCompleted = isCompleted
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id.value,
        title = title,
        createdAt = createdAt.toEpochMilliseconds(),
        isCompleted = isCompleted
    )
}
```

### 4. Repository実装

Domainのインターフェースを実装し、Entity ↔ Model変換を行う。

```kotlin
class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {
    
    override fun getTasks(): Flow<List<Task>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun save(task: Task) {
        dao.insert(task.toEntity())
    }
}
```

---

## ⚠️ マイグレーション

アプリのリリース後にスキーマを変更する場合は、必ず**マイグレーション**が必要です。

1. `Database` クラスのバージョン番号を上げる。
2. `Migration` クラスを定義する。
3. `addMigrations` でビルダーに追加する。

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE tasks ADD COLUMN priority INTEGER DEFAULT 0 NOT NULL")
    }
}
```

---

## ✅ ベストプラクティス

1. **IDはString(UUID)**: オフライン同期や分散システムとの親和性のため、自動インクリメントではなくUUID推奨。
2. **日付はLong(Epoch)**: DB保存時はLong、ドメインでは `kotlinx.datetime.Instant` や `LocalDateTime` を使用。
3. **トランザクション**: 複数の更新を行う場合は `@Transaction` を使用して整合性を保つ。
