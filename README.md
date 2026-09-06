# Stacked PR実験

AIが生成した変更をstacked PRでレビューする流れを検証するための、最小構成のSpring Bootアプリケーションです。

## プロジェクト構成

- Controller層: `task.controller`
- Service層: `task.service`
- Logic層: `task.logic`
- Entity層: `task.entity`
- Repository層: `task.repository`

## ローカルでの確認

```bash
docker compose up -d
./mvnw test
./mvnw spring-boot:run
docker compose down
```

アプリケーションとテストはPostgreSQLを使用します。ローカルのデフォルト接続先は
`jdbc:postgresql://localhost:5432/prtest`、ユーザー名とパスワードは
`prtest`です。これらの値は`SPRING_DATASOURCE_*`環境変数で変更できます。

ブラウザを使ったE2Eテストは、PostgreSQLを起動した状態で次のコマンドを実行します。
初回またはPlaywrightのバージョン更新時には、先にChromiumをインストールします。

```bash
./mvnw test-compile exec:java -Dexec.classpathScope=test -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
./mvnw -Pe2e verify
```

[テスト戦略](docs/testing-strategy.md)に、テストの責務、ローカル/CIでの実行方針、
stacked PRのレビュー手順をまとめています。
Stacked PRのAIレビュー、次層への進行条件、rebase方針は
[Stacked PR運用ガイド](docs/stacked-pr-workflow.md)を参照してください。

## GitHub Actions

CIワークフローは、すべてのpushとPull Requestで標準のMavenテストを実行します。

## 実験の方針

AIに小さな機能単位の実装を生成させ、stacked PRで変更をレビューします。
人間のレビュアーは、層の責務分担と統合境界を確認します。
