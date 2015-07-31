join-test-template
【できること】
Oracleスキーマからテーブル、カラムの論理名物理名を取り出して、
(旧)結合テスト仕様書フォーマットに書き出してくれます。

【使用方法】
①join-test-template を任意のローカルのフォルダにコピー
②join-test-template\springconf\applicationContext-datasource-local.xml を編集する。

	<!-- TODO↓のuser/passwordを自分のユーザ/パスワードに書き換える -->
      <property name="user"                     value="fixme!!"/>
      <property name="password"                 value="fixme!!"/>

③join-test-template\springconf\applicationContext-join-test.xml を編集する。
  ownerは自分のスキーマを推奨(任意のバージョンに対応)。
  tableIds には、必要なテーブル物理名をカンマ区切りで列挙(改行してもOK)。

    <property name="owner" value="SCHM_M001_SCS"/>
    <property name="tableIds" value="ME0039,MR0008,ME1028,ME0009,ME1029,CR0006,MR0016,CR1004"/>

③’Excel出力先フォルダを作成し以下に出力先を設定する。
    <property name="inputPath" value="D:\join-test-template\join-test-template\out"/>

④join-test-template\template.bat を起動
⑤join-test-template\out に出力されたExcelシートを自由に使用(手動テスト、Criteria記述書のエビ)

※修正する２つのxmlファイルに日本語は記述しないで下さい。

