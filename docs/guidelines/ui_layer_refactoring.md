# UI Layer Refactoring Guidelines

This document defines the standard for organizing UI components within feature modules to ensure a high degree of **Separation of Concerns (SoC)** and maintainability.

## 1. Pattern Overview: Screen-Component Separation

Each feature module must separate its UI into a root **Screen** and specialized **Components**.

### 1.1. The Root Screen (`*Screen.kt`)
**Role**: Coordinator and State Provider.
- Fetches state from the ViewModel.
- Orchestrates side effects (Navigations, Snackbars).
- Provides high-level layout structure using `YatteScaffold` or `YatteBasicScaffold`.
- **Target Line Count**: < 100 lines.

### 1.2. The Component Package (`component/*.kt`)
**Role**: Pure UI Presentation and Logic Extraction.
- Large UI blocks or complex logic must be split into individual files.
- Components should be modular and, where possible, stateless (relying on parameters).

## 2. File Naming Conventions

Internal components should follow these naming patterns:

| File Name | Responsibility |
| :--- | :--- |
| `*Header.kt` | Configuration of the TopAppBar, navigation icons, and title logic. |
| `*SideEffects.kt` | Coordination of `LaunchedEffect` for handling ViewModel events (navigation, snackbars). |
| `*Content.kt` | The main body of the screen (List, Pager, Scrollable content). |
| `*Dialogs.kt` | All dialogs and bottom sheets related to the screen. |
| `*Sections.kt` | Static sections of a screen (if too small for individual files). |
| `*Item.kt` | Specialized UI for list/grid items. |

## 3. Implementation Example

### [CategoryScreen.kt](file:///home/segnities007/AndroidStudioProjects/yatte/presentation/feature/category/src/commonMain/kotlin/com/segnities007/yatte/presentation/feature/category/CategoryScreen.kt) (Root)
```kotlin
@Composable
fun CategoryScreen(...) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    CategorySetupSideEffects(...)
    
    YatteBasicScaffold(
        topBar = { CategoryTopBar(...) },
        floatingActionButton = { CategoryFab(...) }
    ) { padding ->
        CategoryContent(state, ..., padding)
        CategoryDialogs(state, ...)
    }
}
```

## 4. Benefits
- **Readability**: Small files are easier to scan and understand.
- **Testability**: Components can be tested or previewed in isolation.
- **Maintainability**: Changes to specific UI parts (e.g., a dialog) don't affect unrelated code in the root screen.
- **Consistency**: All features follow the same architectural blueprint.
