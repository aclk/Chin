2009年9月4日 17:57:46 SCS三浦一仁

P6SpyとLog4jの使い方としては「ログに混ざって欲しくないから、あるファイルに纏めて書き出したい」だと思われます。
そこで、P6spyと、ついでにlog4jで、「あるファイルに吐く設定例」を紹介します。

・P6Spyの設定方法

1.spy.propertiesファイルを、
[プロジェクトフォルダ]\src\main\resources\
に上書きコピーします。

2.出力ファイルのパスなど、設定を自分色に染めます。

3.データベースの接続ファイル
[プロジェクトフォルダ]\src\main\resources\springconf\applicationContext-datasource-local.xml
を、以下のように変更します。

		<property name="driverClass" value="oracle.jdbc.driver.OracleDriver" />		←こっちを殺して
		<!--
		<property name="driverClass" value="com.p6spy.engine.spy.P6SpyDriver"/>		←こっちを生かす
		 -->
		 

・Log4jの設定方法

Wiki参照