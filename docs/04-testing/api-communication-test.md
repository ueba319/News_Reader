# API通信テスト仕様書

## 1. 単体テスト

| 対象 | 確認内容 |
|---|---|
| GNewsArticleMapper | DTOの各項目とカテゴリがドメインモデルへ変換される |
| NewsArticleSelector | 経済情勢キーワードに一致する記事が優先される |
| NewsArticleSelector | 重複を除外し、異なる配信元が優先される |
| NewsViewModel | 起動時に過去24時間を指定する |
| NewsViewModel | 記事取得成功時にSuccessになる |
| NewsViewModel | 国内・海外が空の場合にEmptyになる |
| NewsViewModel | Repository例外時にErrorになる |

実行コマンド：

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

## 2. Android UIテスト

本番の `NewsRepository` をHiltのテストモジュールでFake Repositoryに置き換える。GNews APIへの通信やAPIキーを使用せず、以下を確認する。

| 対象 | 確認内容 |
|---|---|
| アプリ起動 | ヘッダータイトルが表示される |
| 国内ニュース | Fake Repositoryの記事タイトルが表示される |

実行コマンド：

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

## 3. 手動確認

1. APIキーを設定したDebugアプリを起動する。
2. 国内記事が最大5件表示されることを確認する。
3. 海外タブへ切り替え、英語記事が最大5件表示されることを確認する。
4. 記事を選択し、ブラウザで元記事が開くことを確認する。
5. 通信失敗時にエラー表示と再試行ボタンが表示されることを確認する。
