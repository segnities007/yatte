# テストガイドライン

品質を担保するため、適切なテスト戦略を実施します。

## 🧪 テスト戦略

| テストレベル | 対象 | ツール | 目的 |
|-------------|------|-------|------|
| **Unit Test** | Domain (UseCase), ViewModel (State) | JUnit, Kotest, Mockk | ロジックの正しさ検証（高速） |
| **Integration** | Repository (Room) | AndroidJUnit4, Room | DBの読み書き検証 |
| **UI Test** | Compose Screen | Compose UI Test | 表示・操作の検証 |

現在のフェーズでは **Unit Test** を最優先とします。

---

## 🛠️ ユニットテストの実装

`commonTest` ソースセットに記述します。

### 1. ViewModelのテスト (State検証)

ViewModelのテストでは、Intentを送信した結果、期待するStateになるかを検証します。

```kotlin
class TaskListViewModelTest {
    
    @Test
    fun `load tasks success`() = runTest {
        // Arrange
        val mockRepo = mockk<TaskRepository>()
        coEvery { mockRepo.getTasks() } returns flowOf(listOf(testTask))
        val viewModel = TaskListViewModel(GetTasksUseCase(mockRepo))

        // Act
        viewModel.onIntent(TaskListIntent.LoadData)

        // Assert
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(1, state.tasks.size)
    }
}
```

### 2. UseCaseのテスト

ビジネスロジックの境界値や条件分岐をテストします。

```kotlin
class CreateTaskUseCaseTest {
    
    @Test
    fun `create task fails if title is empty`() = runTest {
        val useCase = CreateTaskUseCase(mockk())
        
        val result = useCase(title = "")
        
        assertTrue(result.isFailure)
    }
}
```

---

## 📝 命名規則

テストメソッド名は、バッククォートを使用し、Behavior Driven Development (BDD) スタイルの **GIVEN-WHEN-THEN** 形式で記述することを**義務付けます**。

**必須フォーマット:**
`` `GIVEN 英語で与えられた条件 WHEN テストするケース THEN 期待する結果` ``

**例:**
```kotlin
@Test
fun `GIVEN tasks exist WHEN loading tasks THEN returns task list`() = runTest { ... }

@Test
fun `GIVEN empty title WHEN creating task THEN returns failure`() = runTest { ... }
```

---

## ✅ ベストプラクティス

1. **Arrange-Act-Assert (AAA) コメントの強制**: テスト内は必ず3つのセクションに分け、`// Arrange` (または GIVEN), `// Act` (または WHEN), `// Assert` (または THEN) のコメントを記述すること。

```kotlin
@Test
fun `GIVEN valid task WHEN save called THEN saves to db`() = runTest {
    // Arrange
    val task = Task(...)
    
    // Act
    repository.save(task)
    
    // Assert
    verify { dao.insert(any()) }
}
```

2. **高速に保つ**: DB接続やネットワーク通信を伴うテストはUnit Testに含めない（Mockを使用）。
3. **決定論的**: いつ実行しても同じ結果になるようにする（`runTest`の仮想時間制御を活用）。

