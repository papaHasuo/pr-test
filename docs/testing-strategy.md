# テスト戦略

## 目的

このプロジェクトでは、アプリケーション、ローカル開発、CIのすべてでPostgreSQLを使用します。
stacked PRをレビューしやすい大きさに保ちながら、ビジネスルールの退行と
統合上の問題を検出することが目的です。

## テストの層

| テスト | 保証する内容 | 現在の状態 | 実行場所 |
| --- | --- | --- | --- |
| Logic単体テスト | 状態遷移と入力ルール | 専用テストはまだない | ローカル、すべてのPR |
| Service単体テスト | ユースケースの制御とRepository連携 | `TaskServiceTest`が作成処理の一部を検証。Repositoryを使うケースは拡充予定 | ローカル、すべてのPR |
| Controllerテスト | リクエストマッピング、リダイレクト、Modelの契約 | `TaskControllerTest`が一覧画面の契約を検証 | ローカル、すべてのPR |
| Springコンテキスト/統合テスト | Bean構成、JPAマッピング、PostgreSQL互換性 | `PrTestApplicationTests`がアプリケーション全体のコンテキストを起動 | PostgreSQL上でローカル、すべてのPR |
| HTTP/Thymeleaf統合テスト | HTTPからController、Service、DB、HTML生成までの契約 | UI変更時に追加 | PostgreSQL上でローカル、すべてのPR |
| ブラウザ/一連のE2Eテスト | ユーザーから見たタスク操作 | `TaskCompletionE2ETest`がPlaywrightとChromiumで完了フローを検証 | UI変更時とmainへのマージ時 |

テストは、その振る舞いを表現できる最も狭い層で検証します。
アプリケーション全体のコンテキストテストは単体テストの代わりにはならず、
単体テストもPostgreSQLを使うコンテキストテストの代わりにはなりません。

## ローカルでの実行

アプリケーションとCIで使うものと同じデータベースを起動します。

```bash
docker compose up -d
./mvnw test
./mvnw spring-boot:run
docker compose down
```

デフォルトの接続先は`localhost:5432`の`prtest`データベースで、
ユーザー名とパスワードも`prtest`です。必要に応じて
`SPRING_DATASOURCE_URL`、`SPRING_DATASOURCE_USERNAME`、
`SPRING_DATASOURCE_PASSWORD`で上書きします。

実装中は、フィードバックを速くするため対象テストだけを実行できます。

```bash
./mvnw -Dtest=TaskServiceTest test
```

PRを作成・更新する前には、`./mvnw test`で全テストを実行します。

ブラウザE2Eテストを実行する場合は、PostgreSQLを起動した状態で専用プロファイルを使用します。
初回またはPlaywrightのバージョン更新時には、先にChromiumをインストールします。

```bash
./mvnw test-compile exec:java -Dexec.classpathScope=test -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
./mvnw -Pe2e verify
```

## AI実装時のテスト手順

AIに実装を依頼する場合も、実装とテスト実行を一つの作業単位として扱います。

1. 実装前に、変更対象の層と既存テストを確認する。
2. 実装中は、変更対象に近いテストを実行して速くフィードバックを得る。
3. コミット前に、PostgreSQLを起動した状態で`./mvnw test`を実行する。
4. PR本文に、実行したテストコマンドと結果、未実施のテストを記載する。
5. 下位層の契約を変更した場合は、後続PRのテストと実装への影響を確認する。

層ごとの重点は次のとおりです。

| 変更層 | 実装中に優先するテスト | PRで確認するテスト |
| --- | --- | --- |
| Domain/Logic | 状態遷移・入力ルールの単体テスト | 高速テストと既存回帰テスト |
| Service | Service単体テスト | PostgreSQLを使う統合テストを含む全テスト |
| Controller | Controller単体テスト、HTTPレベルのテスト | PostgreSQLを使う統合テストを含む全テスト |
| UI | 画面契約と重要な操作の確認 | 必要なE2Eと全テスト |

E2Eテストは、すべての変更で実行するのではなく、UIの重要なユーザーシナリオを
変更した場合に限定します。AIの実装ごとにE2Eを必須化すると、フィードバックが
遅くなり、テストの保守コストが実験の目的を上回るためです。

## CIでの実行

GitHub ActionsはPostgreSQL 16をサービスコンテナとして起動し、
ヘルスチェックを待ってから、ローカルと同じMavenテストを実行します。
これにより、CIの成功はJavaのコンパイルだけでなく、JPAマッピングや
データベースの振る舞いも確認できたことを意味します。

CIは決定的に実行できるようにし、開発者のローカルデータベースや
未コミットのスキーマ変更に依存させません。通常のMavenテストとは別に、
ブラウザE2E用のジョブを用意し、PostgreSQLとChromiumを起動して実行します。

## Stacked PRの進め方

各PRでテストが成功する状態を維持します。

1. Domain/Logic PR: 状態遷移ルールと単体テストを追加する。
2. Service PR: 永続化を含むユースケースとRepository連携テストを追加する。
3. Controller PR: エンドポイントとControllerテストを追加する。
4. UI PR: ユーザー操作とHTTP/Thymeleaf統合テストを追加する。
5. E2E用Sub-Issue: 重要なユーザーシナリオをブラウザで検証する。

PR本文には、ベースブランチ、親PR、実行したテストコマンド、
既知の制約を記載します。レビュアーはstackの下位から順にマージします。
下位PRが契約を変更した場合は、後続PRをrebaseし、テストを更新してから
全テストを再実行し、再レビューを依頼します。

## 今後のCI拡張

通常の`mvnw test`とブラウザE2Eを別ジョブで実行します。E2Eは実ブラウザを
起動するため、通常のテストより時間がかかる可能性がありますが、UIの重要な
ユーザーシナリオを変更したPRでは実行結果を確認します。CIではE2Eジョブの
中でPlaywrightのChromiumをインストールしてから専用プロファイルを実行します。
