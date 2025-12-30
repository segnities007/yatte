# UI実装ガイドライン

yatteでは**Compose Multiplatform**を使用し、**MVI (Model-View-Intent)**パターンでUIを実装します。

## 🎨 デザイン原則

- **シンプル & クリーン**: 余白を十分に取り、要素を詰め込みすぎない。
- **Android/iOS準拠**: プラットフォーム固有の挙動（バックハンドリングなど）を尊重するが、見た目は統一する。
- **Atomic Design**: 再利用可能なコンポーネントを `:presentation:common:components` に定義する。

---

## 🔄 MVI パターン実装

各画面は単方向データフロー（Unidirectional Data Flow）に従います。

### 1. State (状態)
UIのその時点でのすべての状態を表す不変データクラス。

```kotlin
data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Boolean = false // UIイベントもStateで管理
)
```

### 2. Intent (意図/アクション)
ユーザーインタラクションやシステムイベントを表す Sealed Interface。

```kotlin
sealed interface TaskListIntent {
    data object LoadData : TaskListIntent
    data class OnTaskClick(val taskId: TaskId) : TaskListIntent
    data class OnDeleteConfirm(val taskId: TaskId) : TaskListIntent
    data object OnErrorDismiss : TaskListIntent
}
```

### 3. ViewModel
Intentを受け取り、UseCaseを実行し、Stateを更新する。

```kotlin
class TaskListViewModel(
    private val getTasks: GetTasksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskListState())
    val state = _state.asStateFlow()

    fun onIntent(intent: TaskListIntent) {
        when (intent) {
            is TaskListIntent.LoadData -> loadTasks()
            is TaskListIntent.OnTaskClick -> ...
        }
    }
    
    private fun loadTasks() {
        // State更新は .update {} を使用
        _state.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            // UseCase実行
            val result = getTasks()
            _state.update { 
                it.copy(isLoading = false, tasks = result) 
            }
        }
    }
}
```

### 4. Screen (Compose UI)
Stateを観測し、Intentを発行する。

```kotlin
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    TaskListContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

// プレビュー用にStateとLambdaを受け取るコンポーネントに分離する
@Composable
fun TaskListContent(
    state: TaskListState,
    onIntent: (TaskListIntent) -> Unit
) {
    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn { ... }
    }
}
```

---

## 🖼️ プレビュー

Compose Multiplatformでは、`commonMain` でプレビューを使用できます（Android Studio Koala Feature Drop以降）。

```kotlin
@Preview
@Composable
fun TaskListContentPreview() {
    AppTheme {
        TaskListContent(
            state = TaskListState(
                tasks = listOf(Task(id = "1", title = "Test Task"))
            ),
            onIntent = {}
        )
    }
}
```

---

## ✅ ベストプラクティス

1. **ViewModelにContextを持ち込まない**: Android固有の依存を持たせない。
2. **Stateは不変(Immutable)**: `val`のみを使用し、`copy()`で更新する。
3. **ロジックをUIに書かない**: `if`分岐などは最小限にし、ViewModelで計算済みの値をStateに入れる。
4. **リソースアクセス**: `stringResource`, `painterResource` などを使用し、ハードコーディングしない。
   ```kotlin
   // ❌ ハードコーディング
   Text(text = "保存")
   
   // ✅ リソース使用
   Text(text = stringResource(Res.string.common_save))
   ```
5. **UIロジックとビジュアルの分離**
   - 複雑な計算やフォーマット処理はコンポーザブル内に直接書かず、ViewModelやUtilityクラスに分離してください。
   - 例: 日付フォーマットは `DateFormatter` などの共通ユーティリティを使用する。

6. **Compose Multiplatform Resourcesの注意点**
   - `getString()` は `suspend` 関数です。`onClick` やコールバック（非サスペンドスコープ）で使用する場合は、`rememberCoroutineScope` を使用して `launch` ブロック内で呼び出してください。
   ```kotlin
   val scope = rememberCoroutineScope()
   // ...
   onClick = {
       scope.launch {
           val message = getString(Res.string.message)
           snackbarHostState.showSnackbar(message)
       }
   }
   ```
