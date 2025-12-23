<div align="center">
  <h1>🔔 yatte (やって)</h1>
  <h3>Clear Manager - タスクが「溜まらない」シンプルな管理アプリ</h3>

  <p>
    <a href="https://github.com/segnities007/yatte/actions">
      <img src="https://img.shields.io/github/actions/workflow/status/segnities007/yatte/build-test.yml?branch=main&label=Build&style=flat-square" alt="Build Status" />
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License" />
    </a>
    <img src="https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
    <img src="https://img.shields.io/badge/Compose-Multiplatform-4285F4.svg?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Compose Multiplatform" />
  </p>

  <p>
    <a href="#features">Features</a> •
    <a href="#tech-stack">Tech Stack</a> •
    <a href="#getting-started">Getting Started</a> •
    <a href="#documentation">Documentation</a>
  </p>
</div>

---

## 📖 About

**yatte（やって）** は、「やりっぱなしを防ぐ」「タスクリストが溜まっていくストレスを解消する」ことをテーマにした、新しいタスク管理アプリです。

従来のTODOアプリとは異なり、設定したアラーム時刻から24時間経過したタスクは**自動的に削除**されます。「今日やるべきこと」だけに集中できる、クリーンな状態を常に維持します。

### ✨ Key Concept
*   **Prevent Accumulation**: タスクが溜まらない自動削除メカニズム
*   **Stress Free**: 未完了タスクの山に押しつぶされない
*   **Clean & Simple**: "やわらかく、クリアで、きれいな" UIデザイン

---

## 📱 Screenshots

| Home (Start List) | History (Timeline) | Settings |
|:-----------------:|:------------------:|:--------:|
| <img src="docs/images/home_mock.png" width="200" alt="Home Screen" /> | <img src="docs/images/history_mock.png" width="200" alt="History Screen" /> | <img src="docs/images/settings_mock.png" width="200" alt="Settings Screen" /> |

*(Screenshots coming soon)*

---

## 🚀 Features

*   **スタートリスト**: 1回だけのタスクと、毎週のルーチンタスクを管理
*   **スマートアラーム**: 設定時刻の n分前 に通知（カスタマイズ可能）
*   **自動クリーンアップ**: アラーム後24時間でリストから自動消去
*   **履歴タイムライン**: 完了したタスクを時系列グラフで振り返り
*   **クロスプラットフォーム**: Android, iOS, Desktop (Windows/Mac/Linux) で完全に動作

---

## 🛠 Tech Stack

このプロジェクトは **Compose Multiplatform** を使用したモダンなクロスプラットフォーム開発を採用しています。

| Category | Tech |
|----------|------|
| **Language** | ![Kotlin](https://img.shields.io/badge/-Kotlin_2.0-7F52FF?logo=kotlin&logoColor=white) |
| **UI Framework** | ![Compose](https://img.shields.io/badge/-Compose_Multiplatform-4285F4?logo=jetpackcompose&logoColor=white) |
| **Architecture** | Clean Architecture, MVI (Model-View-Intent) |
| **DI** | [Koin](https://insert-koin.io/) |
| **Database** | [Room](https://developer.android.com/training/data-storage/room) (KMP) |
| **CI/CD** | GitHub Actions |

---

## 🏁 Getting Started

### Prerequisites
*   JDK 11+
*   Android Studio Ladybug or later (recommended)
*   Xcode 15+ (for iOS build)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/segnities007/yatte.git
cd yatte

# Android
./gradlew composeApp:installDebug

# Desktop (Run)
./gradlew composeApp:run
```

### Development Guidelines

開発に参加する場合は、必ず以下のガイドラインを参照してください：
*   [Development Guidelines (AGENTS.md)](AGENTS.md)

---

## 📚 Documentation

詳細な仕様書は `docs/` ディレクトリにあります。

*   [コンセプト (Concept)](docs/concept.md)
*   [機能仕様 (Features)](docs/features.md)
*   [画面仕様 (Screens)](docs/screens.md)
*   [UIデザイン (UI Design)](docs/ui-design.md)
*   [技術仕様 (Technical)](docs/technical.md)

---

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <sub>Built with ❤️ by segnities007</sub>
</div>
