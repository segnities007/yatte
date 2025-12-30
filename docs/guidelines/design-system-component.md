# デザインシステム コンポーネント作成ガイドライン

Yatteデザインシステムにおけるコンポーネント作成のベストプラクティス。

---

## 📁 ディレクトリ構造

```
presentation/designsystem/src/commonMain/kotlin/.../component/
├── button/      # ボタン系 (YatteButton, YatteIconButton, etc.)
├── card/        # カード系 (YatteCard, YatteSectionCard)
├── input/       # 入力系 (YatteTextField, YatteSlider, YatteChip, etc.)
├── navigation/  # ナビゲーション系 (YatteFloatingHeader, YatteFloatingNavigation)
├── feedback/    # フィードバック系 (YatteDialog, YatteLoadingIndicator, etc.)
└── layout/      # レイアウト系 (YatteScaffold, YatteSoundPicker)
```

---

## 🎯 コンポーネント作成の原則

### 1. 命名規則

```kotlin
// ✅ 良い例
fun YatteButton(...)       // プレフィックス "Yatte" + 役割名
fun YatteIconButton(...)

// ❌ 悪い例
fun CustomButton(...)      // プレフィックスなし
fun MyButton(...)
```

### 2. パッケージ宣言

```kotlin
// カテゴリに応じたサブパッケージを使用
package com.segnities007.yatte.presentation.designsystem.component.button
package com.segnities007.yatte.presentation.designsystem.component.input
```

### 3. KDoc コメント

```kotlin
/**
 * Yatte統一〇〇コンポーネント
 *
 * [使用場面の説明]
 *
 * @param onClick クリック時のコールバック
 * @param text ボタンのテキスト
 */
@Composable
fun YatteButton(...)
```

---

## 🎨 デザイントークンの適用

### bounceClick の適用

全てのインタラクティブ要素には `bounceClick` を適用する。

```kotlin
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick

@Composable
fun YatteButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.bounceClick(),  // ✅ 必須
    ) {
        Text(text)
    }
}
```

### カラートークンの使用

ハードコードされた色ではなく、MaterialTheme のカラートークンを使用する。

```kotlin
// ✅ 良い例
color = MaterialTheme.colorScheme.primary

// ❌ 悪い例
color = Color(0xFF4CAF50)
```

### スペーシングトークンの使用

```kotlin
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

// ✅ 良い例
padding = YatteSpacing.md    // 16.dp
padding = YatteSpacing.sm    // 8.dp

// ❌ 悪い例
padding = 16.dp
```

---

## 📐 コンポーネントの署名パターン

### 基本パターン

```kotlin
@Composable
fun YatteXxx(
    // 1. 必須パラメータ（コールバック、データ）
    onClick: () -> Unit,
    text: String,
    
    // 2. Modifier（デフォルト値あり）
    modifier: Modifier = Modifier,
    
    // 3. オプショナルパラメータ（デフォルト値あり）
    enabled: Boolean = true,
    contentDescription: String? = null,
)
```

### ジェネリックパターン（リスト選択系）

```kotlin
@Composable
fun <T> YatteSegmentedButtonRow(
    options: List<T>,
    selectedIndex: Int,
    onOptionSelected: (Int, T) -> Unit,
    optionLabel: @Composable (T) -> String,  // ラベル生成関数
    modifier: Modifier = Modifier,
)
```

---

## ✅ チェックリスト

新しいコンポーネントを作成する際は、以下を確認してください：

- [ ] プレフィックス `Yatte` がついている
- [ ] 適切なサブパッケージに配置している
- [ ] KDoc コメントを記述している
- [ ] `bounceClick` を適用している（インタラクティブ要素の場合）
- [ ] MaterialTheme のカラートークンを使用している
- [ ] YatteSpacing を使用している
- [ ] Modifier パラメータにデフォルト値を設定している
- [ ] **同一ファイル内に `@Preview` を作成している**

---

## 👀 Preview ガイドライン

### 基本ルール

**Preview は各コンポーネントファイルの末尾に `private` で定義する。**

```kotlin
// YatteButton.kt

@Composable
fun YatteButton(...) { ... }

// ✅ 同一ファイル内に Preview を配置
@Preview(showBackground = true)
@Composable
private fun YatteButtonPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        YatteButton(text = "Primary", onClick = {})
        YatteButton(text = "Disabled", onClick = {}, enabled = false)
    }
}
```

### 必要な依存関係

`build.gradle.kts`:
```kotlin
commonMain.dependencies {
    implementation(compose.components.uiToolingPreview)  // Preview アノテーション
}

androidMain.dependencies {
    implementation(compose.uiTooling)  // ツーリング実装
}
```

### 命名規則

- `{コンポーネント名}Preview` で統一
- 複数バリエーションがある場合は `{コンポーネント名}VariantsPreview` など

### 禁止事項

- ❌ 別ファイルへの分離（`YatteDesignSystemPreviews.kt` 等は禁止）
- ❌ `public` なPreview関数

---

## 🔄 既存コンポーネントの抽象化手順

Feature層で直接Material3コンポーネントを使用している箇所を見つけたら：

1. **パターンを特定**: 同じ使い方が複数箇所にあるか確認
2. **デザインシステムコンポーネントを作成**: 上記ガイドラインに従う
3. **bounceClick を適用**: インタラクティブ要素には必須
4. **Feature層を更新**: import文とコンポーネント呼び出しを置き換え
5. **ビルド確認**: `./gradlew assembleDebug`

---

*更新日: 2025-12-29*
