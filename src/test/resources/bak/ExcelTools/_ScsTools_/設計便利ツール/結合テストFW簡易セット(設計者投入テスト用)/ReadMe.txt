結合テストFW簡易セット(設計者投入テスト用)　使用方法 			2007年7月24日 三浦一仁

■前提条件

「クライアント環境構築ガイド」における
「VII	Oracle Instant Client」の章の設定が完了している事を前提とします。


■使い方

1,
.\webapps\join-test\WEB-INF\classes\springconf の

applicationContext-datasource-local.xml
applicationContext-join-test.xml

を、「結合テストテンプレート出力」と同様、自身環境の設定に変更し、
フォルダ直下のstartup.batを起動してください。


2, 

起動できたら、

http://localhost:8080/join-test/mock/frameMock.html

の左下フレームのみ操作し、データ投入してください。




■参考資料

結合テストガイド_実施編.doc


以上。