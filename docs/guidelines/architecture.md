# アーキテクチャガイドライン

yatteプロジェクトでは**Clean Architecture**をベースにしたレイヤー構成を採用しています。

## 🏗️ レイヤー構成

プロジェクトは3つの主要なレイヤーに分割されます。

```
Presentation → Domain ← Data
```

| レイヤー | パッケージ | 役割 | 依存ルール |
|---------|------------|------|------------|
| **Presentation** | `:presentation` | UI描画、ユーザー入力ハンドリング、状態管理 | Domainに依存 |
| **Domain** | `:domain` | ビジネスロジック、データモデル、インターフェース定義 | **依存なし（純粋なKotlin）** |
| **Data** | `:data` | データアクセス実装、DB操作、API通信 | Domainに依存 |

### ⚠️ 依存関係の鉄則
1. **外側（Presentation/Data）から内側（Domain）への依存のみ**を許可する。
2. Domainレイヤーは他のレイヤーやフレームワーク（Android SDK, Composeなど）を知らない。
3. DataレイヤーとPresentationレイヤーは直接依存しない。必ずDomainを経由する。

---

## 🧩 ディレクトリ構造とモジュール分割

機能（Feature）と集約（Aggregate）に基づいてパッケージを分割します。

### Presentation層: Feature単位
画面や機能単位でパッケージを切ります。

```
:presentation:feature:home/
├── HomeScreen.kt          # UI (Compose)
├── HomeViewModel.kt       # 状態管理
├── HomeState.kt           # UI状態
└── HomeIntent.kt          # ユーザーアクション
```

### Domain/Data層: Aggregate単位
データの整合性を保つ単位（集約）でパッケージを切ります。

```
:domain:aggregate:task/
├── model/Task.kt         # ドメインモデル
├── usecase/              # ビジネスロジック
│   ├── CreateTask.kt
│   └── GetTasks.kt
└── repository/           # インターフェース
    └── TaskRepository.kt

:data:aggregate:task/
├── repository/           # 実装
│   └── TaskRepositoryImpl.kt
└── local/                # データソース
    ├── TaskDao.kt
    └── TaskEntity.kt
```

---

## 🏭 DI (Dependency Injection)

**Koin**を使用して依存関係を注入します。

### モジュール定義
各レイヤー/集約ごとにKoinモジュールを定義します。

```kotlin
val taskModule = module {
    // Data
    single<TaskDao> { get<AppDatabase>().taskDao() }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    
    // Domain (UseCases)
    factory { CreateTaskUseCase(get()) }
    
    // Presentation (ViewModel)
    viewModel { TaskListViewModel(get()) }
}
```

---

## 🔄 データフロー

1. **View**が**Intent**を発行
2. **ViewModel**が**UseCase**を実行
3. **UseCase**が**Repository(Interface)**を呼び出す
4. **Repository(Impl)**が**DataSource**からデータを取得
5. **Data** → **Domain Model**へ変換して返却
6. **ViewModel**が**State**を更新
7. **View**が再描画

---

## ✅ チェックリスト

実装時には以下を確認してください：

- [ ] Domainモデルはデータクラスとして定義されているか（Entityではない）
- [ ] UseCaseは1つのアクションのみを行っているか
- [ ] RepositoryのインターフェースはDomainに、実装はDataにあるか
- [ ] ViewModelはAndroidフレームワークに依存していないか
