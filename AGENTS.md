# AGENTS.md - AI開発アシスタント向けガイドライン

このドキュメントは、AI開発アシスタントが**yatte**プロジェクトを理解し、効果的に支援するためのガイドラインです。

> [!IMPORTANT]
> **作業を開始する前に**
> 担当する領域のガイドラインを**必ず**参照してください。実装方針の自己判断によるブレを防ぐためです。
>
> - **コーディング全般**: [コーディング規約](docs/guidelines/coding.md)
> - **Domain層実装**: [Domain層ガイドライン](docs/guidelines/domain.md)
> - **設計・構成など**: [アーキテクチャガイドライン](docs/guidelines/architecture.md)
> - **画面・UI実装**: [UI実装ガイドライン](docs/guidelines/ui.md)
> - **DB・データ操作**: [データベースガイドライン](docs/guidelines/database.md)
> - **テストコード**: [テストガイドライン](docs/guidelines/testing.md)


## 📋 プロジェクト概要


| 項目 | 内容 |
|------|------|
| アプリ名 | **yatte（やって）** - クリアマネージャー |
| コンセプト | 「やりっぱなしを防ぐ」「溜まらないタスクリスト」 |
| 主要機能 | タスク自動削除（アラーム24時間後）、スタートリスト、履歴タイムライン |
| プラットフォーム | Android / iOS / Desktop（Compose Multiplatform） |
| 言語 | Kotlin |

---

## 🛠️ 技術スタック

| 技術 | 用途 |
|------|------|
| Compose Multiplatform | クロスプラットフォームUI |
| Kotlin 2.0+ | メイン言語 |
| Room 2.7.0+ | ローカルDB（KMP対応版） |
| Koin | 依存性注入（DI） |
| MVI | 状態管理パターン |

---

## 🏗️ アーキテクチャ

### レイヤー構成（Clean Architecture）

```
Presentation → Domain ← Data
              (抽象に依存)
```

| レイヤー | 役割 | 分割基準 |
|---------|------|---------|
| **Presentation** | UI/ViewModel/Screen | Feature（画面）単位 |
| **Domain** | UseCase/Model/Repository Interface | Aggregate（集約）単位 |
| **Data** | RepositoryImpl/DAO/Entity | Aggregate（集約）単位 |

### ディレクトリ構造

```
root/
├── :presentation/
│   ├── :presentation:common/          # 共通コンポーネント・テーマ
│   ├── :presentation:feature:home/    # ホーム画面
│   ├── :presentation:feature:task/    # タスク管理画面
│   ├── :presentation:feature:history/ # 履歴画面
│   └── :presentation:feature:settings/# 設定画面
│
├── :domain/
│   ├── :domain:common/
│   ├── :domain:aggregate:task/        # Task集約
│   ├── :domain:aggregate:history/     # History集約
│   ├── :domain:aggregate:alarm/       # Alarm集約
│   └── :domain:aggregate:settings/    # Settings集約
│
├── :data/
│   ├── :data:common/                  # AppDatabase
│   ├── :data:aggregate:task/
│   ├── :data:aggregate:history/
│   ├── :data:aggregate:alarm/
│   └── :data:aggregate:settings/
│
└── :di/                               # Koin modules
```

---

## 🎨 デザインシステム

### カラーパレット

| 用途 | ライトモード | ダークモード |
|------|-------------|-------------|
| Primary | `#4CAF50` (グリーン) | `#81C784` (ライトグリーン) |
| Secondary | `#FFF8E1` (クリームイエロー) | `#FFE082` (ゴールデンイエロー) |
| Background | `#FFFFFF` | `#121212` |
| Surface | `#F5F5F5` | `#1E1E1E` |

### デザインキーワード

- **やわらかく、クリアで、きれいな**デザイン
- ミニマル、クリーンな余白
- やわらかな角丸（8dp〜16dp）
- 軽やかなシャドウ

---

## 📄 ドキュメント一覧

### 仕様ドキュメント

| ドキュメント | パス | 内容 |
|-------------|------|------|
| README | `README.md` | プロジェクト概要 |
| コンセプト | `docs/concept.md` | アプリの理念・差別化 |
| 機能仕様 | `docs/features.md` | 機能詳細 |
| 画面仕様 | `docs/screens.md` | 画面構成・UX |
| UIデザイン | `docs/ui-design.md` | テーマ・カラー |
| 技術仕様 | `docs/technical.md` | アーキテクチャ詳細 |

### 開発ガイドライン

| ドキュメント | パス | 内容 |
|-------------|------|------|
| コーディング規約 | `docs/guidelines/coding.md` | 命名規則、エラー処理、関数設計 |
| Domain層 | `docs/guidelines/domain.md` | Model、UseCase、Repository設計 |
| アーキテクチャ | `docs/guidelines/architecture.md` | レイヤー構成、依存関係 |
| UI実装 | `docs/guidelines/ui.md` | Compose、テーマ、コンポーネント |
| データベース | `docs/guidelines/database.md` | Room、Entity、DAO設計 |
| テスト | `docs/guidelines/testing.md` | ユニットテスト、Given-When-Then |

---


## ✅ コーディング規約

### 命名規則

| 対象 | 規則 | 例 |
|------|------|-----|
| Feature | `:presentation:feature:XXX` | `:presentation:feature:home` |
| Aggregate | `:domain:aggregate:XXX` | `:domain:aggregate:task` |
| UseCase | `動詞 + 名詞 + UseCase` | `CreateTaskUseCase`, `GetTodayTasksUseCase` |
| ViewModel | `画面名 + ViewModel` | `TaskListViewModel` |
| State | `画面名 + State` | `TaskListState` |
| Intent | `画面名 + Intent` (sealed interface) | `TaskListIntent` |

### MVIパターン

```kotlin
// State - UIの状態を表現
data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Intent - ユーザーアクション
sealed interface TaskListIntent {
    data object LoadTasks : TaskListIntent
    data class CompleteTask(val taskId: TaskId) : TaskListIntent
}

// ViewModel - IntentをStateに変換
class TaskListViewModel(...) : ViewModel() {
    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state.asStateFlow()
    
    fun onIntent(intent: TaskListIntent) { ... }
}
```

### エラーハンドリング

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
```

---

## ⚠️ 重要な制約

### やってはいけないこと

- ❌ Domainレイヤーから他レイヤーへの依存
- ❌ Data/Presentationレイヤー間の直接依存
- ❌ Android固有APIをcommonMainで使用
- ❌ 複雑な機能追加（シンプルさが最重要）

### 必ず守ること

- ✅ 内向き依存ルール（外→内）
- ✅ Repository Pattern（Interface in Domain, Impl in Data）
- ✅ suspend関数でのDAO定義
- ✅ Platform-specific実装は`expect/actual`で分離

---

## 🔄 開発ワークフロー

### 新機能追加時

1. **Feature定義**: `:presentation:feature:XXX`にScreen/ViewModel/Stateを作成
2. **Domain設計**: 必要な集約でUseCase/Model/Repositoryインターフェースを定義
3. **Data実装**: RepositoryImpl/DAO/Entityを実装
4. **DI設定**: Koinモジュールを更新

### コード変更時のチェックリスト

- [ ] レイヤー依存ルールを守っているか
- [ ] MVIパターンに従っているか
- [ ] Platform-specific実装は分離されているか
- [ ] テストは追加/更新されているか

---

## 🧪 テスト

### テスト配置

| テスト種類 | 配置 |
|-----------|------|
| ユニットテスト | `commonTest` |
| Android固有テスト | `androidTest` |

### テスト方針

- UseCase: 純粋なKotlinなのでcommonTestで
- ViewModel: 状態遷移をテスト
- Repository: DAOをモック

---

## 📱 プラットフォーム固有実装

### Alarm/通知

| プラットフォーム | 技術 |
|----------------|------|
| Android | AlarmManager / WorkManager |
| iOS | UNUserNotificationCenter |
| Desktop | Coroutines + システム通知 |

### 実装パターン

```kotlin
// Domain (expect)
expect class AlarmScheduler {
    fun schedule(task: Task)
    fun cancel(taskId: TaskId)
}

// Android (actual)
actual class AlarmScheduler(context: Context) { ... }

// iOS (actual) 
actual class AlarmScheduler { ... }
```

---

## 📝 よくある質問への回答

### Q: 新しい画面を追加するには？

1. `:presentation:feature:XXX`パッケージを作成
2. `XXXScreen.kt`, `XXXViewModel.kt`, `XXXState.kt`を作成
3. MVIパターンに従ってIntentを定義
4. ナビゲーションに追加

### Q: 新しいデータを永続化するには？

1. `:domain:aggregate:XXX`にModelとRepositoryインターフェースを定義
2. `:data:aggregate:XXX`にEntity、DAO、RepositoryImplを作成
3. `AppDatabase`にDAOを追加
4. Koinモジュールを更新

### Q: プラットフォーム固有のコードが必要な場合は？

1. `commonMain`で`expect`宣言
2. 各プラットフォーム（`androidMain`, `iosMain`, etc.）で`actual`実装
3. テストは可能な限り`commonTest`で

---

*作成日: 2025-12-24*
