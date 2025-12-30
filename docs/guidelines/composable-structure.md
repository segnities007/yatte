# コンポーザブル ファイル構成ガイドライン

コードからUI構造を直感的に理解できるよう、宣言的で一貫性のあるファイル構成を目指します。

---

## 📚 ファイル構成の基本原則

```
1. Package宣言 + Imports
2. 定数・オブジェクト定義 (private val / private object)
3. Public Composable関数（メインエントリポイント）
4. Private Composable関数（サブコンポーネント、トップダウン順）
5. Private ヘルパー関数・拡張関数
6. Preview関数
```

---

## 🌳 宣言的な階層構造

**読む人がUIツリーを頭の中で構築できる順序**で記述します。

```kotlin
// ✅ 良い例: トップダウンで読める
@Composable
fun SettingsScreen(...) {
    SettingsContent(...)
}

@Composable
private fun SettingsContent(...) {
    Column {
        SettingsNotificationSection(...)
        SettingsDataSection(...)
    }
}

@Composable
private fun SettingsNotificationSection(...) { ... }

@Composable
private fun SettingsDataSection(...) { ... }
```

```kotlin
// ❌ 悪い例: ボトムアップ（読みにくい）
@Composable
private fun SettingsDataSection(...) { ... }

@Composable
private fun SettingsNotificationSection(...) { ... }

@Composable
fun SettingsScreen(...) { ... }
```

---

## 📁 ファイル分割の判断基準

| 状況 | 推奨 |
|------|------|
| 100行以下の小さなコンポーネント | 同一ファイル内に private で定義 |
| 再利用される可能性があるコンポーネント | 別ファイルに分離 |
| 画面固有だが100行を超える | `component/` サブディレクトリへ分離 |
| ヘッダー・ダイアログ・サイドエフェクト | `*Header.kt`, `*Dialogs.kt`, `*SideEffects.kt` に分離 |

### 分離された構造の例

```
feature/settings/
├── SettingsScreen.kt           # エントリポイント
├── SettingsViewModel.kt
├── SettingsContract.kt         # State, Intent, Event
└── component/
    ├── SettingsHeader.kt
    ├── SettingsContent.kt
    ├── SettingsDialogs.kt
    ├── SettingsSideEffects.kt
    ├── SettingsNotificationSection.kt
    └── SettingsDataSection.kt
```

---

## 🎯 Design System ファースト原則

> **重要**: アプリ全体でデザインを統一するため、UIコンポーネントは必ずDesign Systemを経由して使用します。

### 絶対ルール

| ルール | 説明 |
|--------|------|
| **素のMaterial3禁止** | `Button`, `Card`, `TextField` 等を直接使用しない |
| **Design System経由** | 必ず `YatteButton`, `YatteCard`, `YatteTextField` 等を使用 |
| **必要なら先に作成** | 必要なコンポーネントがなければ、Design Systemに追加してから使用 |

```kotlin
// ❌ 禁止: Material3を直接使用
import androidx.compose.material3.Button
Button(onClick = { ... }) { Text("保存") }

// ✅ 正解: Design Systemを使用
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
YatteButton(text = "保存", onClick = { ... })
```

### コンポーネント配置ルール

| 使用範囲 | 配置場所 | 例 |
|----------|----------|-----|
| **1箇所のみ（Feature固有）** | `feature/{name}/component/` | `TaskFormScheduleSection` |
| **2箇所以上（共通）** | `designsystem/component/` | `YatteChip`, `YatteCard` |

### Feature component/ の役割

`feature/{name}/component/` は**そのFeature内でのみ使用する独自コンポーネント**を置く場所です。

```kotlin
// ✅ Feature固有コンポーネント（component/に配置）
// - 他のFeatureでは使用しない
// - そのFeatureのドメインに密結合
feature/task/component/
├── TaskFormBasicInfoSection.kt   // タスク作成画面専用
├── TaskFormScheduleSection.kt    // タスク作成画面専用
└── WeekDaySelector.kt            // タスク作成でのみ使用

// ❌ 複数箇所で使用 → Design Systemへ昇格すべき
// WeekDaySelectorが履歴画面でも使われるようになった場合
// → designsystem/component/input/YatteWeekDaySelector.kt に移動
```

### Design Systemへの昇格手順

Feature componentが2箇所以上で使用されるようになったら：

1. **Design Systemへ移動**: `designsystem/component/{category}/Yatte{Name}.kt`
2. **プレフィックス追加**: `WeekDaySelector` → `YatteWeekDaySelector`
3. **bounceClick適用**: インタラクティブ要素には必須
4. **Feature側を更新**: import文を変更

---

## 🧩 サブコンポーネント分離パターン

### 1. Screen → Structure分離パターン

`*Screen.kt` は依存注入とイベント接続のみを行い、UI構造は別ファイルで定義します。

```kotlin
// SettingsScreen.kt
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsHeader()
    SettingsDialogs(state, viewModel::onIntent)
    SettingsSideEffects(viewModel, ...)

    YatteScaffold(...) { padding ->
        SettingsContent(state, viewModel::onIntent, padding)
    }
}
```

```kotlin
// component/SettingsContent.kt
@Composable
fun SettingsContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    contentPadding: PaddingValues,
) {
    Column(modifier = Modifier.padding(contentPadding)) {
        SettingsNotificationSection(state, onIntent)
        SettingsDataSection(state, onIntent)
    }
}
```

### 2. Header/Dialogs/SideEffects分離パターン

```kotlin
// component/SettingsHeader.kt
@Composable
fun SettingsHeader() {
    val setHeaderConfig = LocalSetHeaderConfig.current
    SideEffect { setHeaderConfig(HeaderConfig(title = { Text("設定") })) }
}

// component/SettingsDialogs.kt
@Composable
fun SettingsDialogs(state: SettingsState, onIntent: (SettingsIntent) -> Unit) {
    if (state.showResetDialog) {
        YatteConfirmDialog(...)
    }
}

// component/SettingsSideEffects.kt
@Composable
fun SettingsSideEffects(viewModel: SettingsViewModel, ...) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event -> ... }
    }
}
```

---

## 📐 パラメータ順序の規約

```kotlin
@Composable
fun YatteCard(
    // 1. コールバック（必須）
    onClick: () -> Unit,

    // 2. コンテンツ/データ（必須）
    content: @Composable ColumnScope.() -> Unit,

    // 3. Modifier（デフォルト値あり、常にこの位置）
    modifier: Modifier = Modifier,

    // 4. スタイル/外観オプション
    elevation: Dp = 4.dp,
    shape: Shape = YatteShapes.medium,

    // 5. 状態/動作オプション
    enabled: Boolean = true,
)
```

---

## 🔤 命名規則

| 種類 | 命名パターン | 例 |
|------|-------------|-----|
| 画面エントリ | `{Feature}Screen` | `HomeScreen` |
| コンテンツ本体 | `{Feature}Content` | `HomeContent` |
| セクション | `{Feature}{Section}Section` | `SettingsNotificationSection` |
| ヘッダー設定 | `{Feature}Header` | `HomeHeader` |
| ダイアログ群 | `{Feature}Dialogs` | `CategoryDialogs` |
| サイドエフェクト | `{Feature}SideEffects` | `HomeSideEffects` |
| リスト項目 | `{Item}Card` / `{Item}Item` | `TaskCard`, `HistoryTimelineItem` |

---

## 📝 定数とデフォルト値

ファイルの先頭（Imports直後）に `private val` または `private object` で定義します。

```kotlin
package com.segnities007.yatte.presentation.feature.home.component

import ...

// ✅ ファイル先頭に定数をまとめる
private const val INITIAL_PAGE = 500
private const val PAGE_COUNT = 1000

private object TaskCardDefaults {
    val Elevation = 2.dp
    val CornerRadius = 12.dp
}

@Composable
fun TaskCard(...) {
    YatteCard(elevation = TaskCardDefaults.Elevation, ...)
}
```

---

## ✅ チェックリスト

新しいコンポーザブルファイルを作成する際：

- [ ] Package宣言 → Imports → 定数 → Public関数 → Private関数 → Preview の順序
- [ ] Public Composable から Private Composable へトップダウンで読める
- [ ] 100行を超えるサブコンポーネントは別ファイルに分離
- [ ] パラメータ順序: コールバック → データ → Modifier → オプション
- [ ] 定数は `private val` / `private object` でファイル先頭に定義
- [ ] Feature固有のサブコンポーネントは `component/` ディレクトリへ配置

---

*更新日: 2025-12-30*
