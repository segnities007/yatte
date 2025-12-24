# Domain層ガイドライン

## 目次

1. [概要](#概要)
2. [ディレクトリ構造](#ディレクトリ構造)
3. [Model（ドメインモデル）](#modelドメインモデル)
4. [Value Object（値オブジェクト）](#value-object値オブジェクト)
5. [Repository（リポジトリ）](#repositoryリポジトリ)
6. [UseCase（ユースケース）](#usecaseユースケース)
7. [エラーハンドリング](#エラーハンドリング)
8. [依存関係ルール](#依存関係ルール)

---

## 概要

Domain層はビジネスロジックの中核であり、他のレイヤー（Data/Presentation）に依存しない。

```
Presentation → Domain ← Data
              (抽象に依存)
```

### 原則

| 原則 | 説明 |
|-----|------|
| **純粋Kotlin** | Android/iOSフレームワークに依存しない |
| **不変性** | data class は immutable に設計 |
| **型安全** | ID は value class で型安全に |

---

## ディレクトリ構造

```
domain/
├── core/                        # 共通要素
│   └── error/DomainError.kt
├── aggregate/
│   └── [集約名]/
│       ├── model/              # Entity, Value Object
│       ├── repository/         # インターフェースのみ
│       └── usecase/            # ビジネスロジック
```

---

## Model（ドメインモデル）

### 基本構造

```kotlin
data class Task(
    val id: TaskId,                    // 必須: value class ID
    val title: String,
    val createdAt: LocalDateTime,
    // ...
) {
    init {
        // バリデーション
        require(title.isNotBlank()) { "タイトルは必須です" }
    }

    companion object {
        private const val MAX_TITLE_LENGTH = 100
    }

    // 状態変更メソッド（copy で新インスタンス返却）
    fun complete(): Task = copy(isCompleted = true)
}
```

### ルール

| ルール | 説明 |
|--------|------|
| `init` ブロックでバリデーション | 不正な状態のオブジェクトを生成させない |
| 状態変更は `copy()` | var プロパティは使わない |
| 定数は `companion object` | マジックナンバーを避ける |
| KDoc コメント必須 | 全プロパティに説明を記載 |

---

## Value Object（値オブジェクト）

### ID の実装

```kotlin
@JvmInline
value class TaskId(val value: String) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun generate(): TaskId = TaskId(Uuid.random().toString())
    }
}
```

### ルール

| ルール | 説明 |
|--------|------|
| `@JvmInline` 必須 | ランタイムオーバーヘッドなし |
| `generate()` メソッド | UUID 生成は `kotlin.uuid.Uuid` を使用 |
| **typealias 禁止** | 型安全性が失われるため |

---

## Repository（リポジトリ）

### インターフェース定義

```kotlin
interface TaskRepository {
    // Flow: リアクティブなデータストリーム
    fun getTodayTasks(): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>

    // suspend: 単発の非同期処理
    suspend fun getById(id: TaskId): Task?
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(id: TaskId)
}
```

### ルール

| 用途 | 型 |
|-----|-----|
| **取得（ストリーム）** | `Flow<T>` |
| **単発操作** | `suspend fun` |
| **nullable 戻り値** | `Task?`（見つからない可能性がある場合） |

---

## UseCase（ユースケース）

### 基本構造

```kotlin
class CompleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(taskId: TaskId): Result<Task> =
        runCatching {
            val task = taskRepository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val completedTask = task.complete()
            taskRepository.update(completedTask)
            completedTask
        }
}
```

### ルール

| ルール | 説明 |
|--------|------|
| **1 UseCase = 1 機能** | 単一責任の原則 |
| `operator fun invoke()` | 呼び出しを簡潔に |
| `Result<T>` 戻り値 | 成功/失敗を明示 |
| `runCatching` 使用 | 例外を Result に変換 |
| `EntityNotFoundError` | `IllegalArgumentException` は使わない |

### 命名規則

| パターン | 例 |
|---------|-----|
| 取得 | `GetTaskByIdUseCase`, `GetTodayTasksUseCase` |
| 作成 | `CreateTaskUseCase` |
| 更新 | `UpdateTaskUseCase`, `CompleteTaskUseCase` |
| 削除 | `DeleteTaskUseCase` |

---

## エラーハンドリング

### DomainError 階層

```kotlin
sealed class DomainError(message: String) : Exception(message)

class EntityNotFoundError(entityName: String, id: String)
    : DomainError("$entityName が見つかりません: $id")

class ValidationError(message: String)
    : DomainError(message)

class OperationNotAllowedError(message: String)
    : DomainError(message)
```

### 使い分け

| エラー | 用途 |
|--------|------|
| `EntityNotFoundError` | ID 検索で見つからない |
| `ValidationError` | バリデーション失敗 |
| `OperationNotAllowedError` | 状態により操作不可 |

---

## 依存関係ルール

### 集約間の依存

```kotlin
// ✅ OK: History が TaskId を参照
data class History(
    val taskId: TaskId,  // 他集約の ID は参照可
)

// ❌ NG: History が Task 全体を参照
data class History(
    val task: Task,  // 他集約の Entity は参照不可
)
```

### 循環依存の回避

```
task → history  ✅ OK
history → task  ✅ OK
task ↔ history  ❌ NG（循環依存）
```

**対策**: ViewModel で複数の UseCase を組み合わせる

```kotlin
// ViewModel
val task = completeTaskUseCase(taskId).getOrThrow()
addHistoryUseCase(History(taskId = task.id, ...))
```

---

## チェックリスト

新しいドメインモデルを追加する際：

- [ ] ID は `value class` で実装
- [ ] `init` ブロックでバリデーション
- [ ] 状態変更メソッドは `copy()` で実装
- [ ] Repository Interface は `Flow` / `suspend` 使い分け
- [ ] UseCase は `Result<T>` を返す
- [ ] エラーは `DomainError` サブクラスを使用
- [ ] KDoc コメント記載

---

*作成日: 2025-12-24*
