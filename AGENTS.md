# AGENTS.md - AI開発アシスタント向けガイドライン

このドキュメントは、AI開発アシスタントが **yatte** プロジェクトを理解し、効果的に支援するための**エントリーポイント**です。
詳細な仕様や規約は `docs/` ディレクトリ内の各ドキュメントを参照してください。

> [!IMPORTANT]
> **作業を開始する前に**
> 担当する領域のガイドラインを**必ず**参照してください。実装方針の自己判断によるブレを防ぐためです。

## 📂 ドキュメント構成

| カテゴリ | ドキュメント | 内容 |
|----------|------------|------|
| **概要** | [README.md](README.md) | プロジェクト概要、セットアップ手順 |
| **コンセプト** | [docs/concept.md](docs/concept.md) | アプリの理念・差別化要素 |
| **仕様** | [docs/features.md](docs/features.md) | 詳細な機能要件、ロードマップ |
| **画面** | [docs/screens.md](docs/screens.md) | 画面遷移、UI要件、ステータス |
| **技術** | [docs/technical.md](docs/technical.md) | アーキテクチャ、技術スタック詳細 |
| **デザイン** | [docs/ui-design.md](docs/ui-design.md) | カラーパレット、デザインシステム |

### 開発・実装ガイドライン (`docs/guidelines/`)

| ジャンル | ファイル | 必読ポイント |
|----------|--------|------------|
| **基本規約** | [coding.md](docs/guidelines/coding.md) | 命名規則、KDoc基準、エラー処理 |
| **設計** | [architecture.md](docs/guidelines/architecture.md) | レイヤー構成、依存ルール、MVIパターン |
| **ドメイン** | [domain.md](docs/guidelines/domain.md) | UseCase実装、Model設計 |
| **データ** | [data.md](docs/guidelines/data.md) | Repository実装、Room/DAO |
| **UI** | [ui.md](docs/guidelines/ui.md) | Compose実装、Preview、テーマ |
| **テスト** | [testing.md](docs/guidelines/testing.md) | テスト方針、配置場所 |

---

## ⚠️ 重要な開発ルール

1.  **情報の単一性**: 仕様に関する情報は `docs/` に集約し、このファイル (`AGENTS.md`) には具体的な仕様を書かない。
2.  **既存コードの尊重**: リファクタリング時は既存のアーキテクチャ（Clean Architecture + MVI）を厳守する。
3.  **ドキュメントの更新**: 実装を変更した場合は、必ず対応する `docs/` 内のドキュメントも更新する。

---

*Updated: 2025-12-26*
