・新規作成　梅田さん
・説明追加　前田(沙)
・2009/11/05　前田(沙) 説明追加(MethodTracerを使用している場合)
・2009/11/05　前田(沙) Log4j.xmlにCloseCheckAspectも追加
・2010/03/28　猿橋     バッチ実行closeCheck.bat追加

====================================================================================================
【対応方法】（バッチで対応する場合）
　■手順
　　１．closeCheck.bat内の「_DB_SCHEMA」を利用するスキーマへ書き換える
　　　　※接続先インスタンスは「DB設定書き換え\dbSetting.properties」の"jdbc.url.0"で定義されたインスタンスが指定される
　　２．closeCheckerへPATHを通す（文字化けになるのでこのPATH上に日本語が存在しないようにして下さい）
　　３．対象プロダクトをチェックアウト
　　４．対象プロダクトをチェックアウトしたフォルダで closeCheck.bat [productID] を実行（回帰テストが走ります）
　　　　ex) closeCheck sjo0010
　　　　　　※プロダクトIDは複数指定可能（closeCheck sjo0010 sjo0020 sjo0030・・・）
　
　■補足
　　closeCheck.batでは pom.xml、log4j.xmlを書き換えております。
　　実行中は元ファイルを "*.bak" ファイルに退避していますが、バッチ実行を途中で強制終了した場合は、
　　cleanupForCloseCheck.bat を実行すると "*.bak" ファイルを 元のファイルへ戻します。


【注意事項】
　●対象プロダクトがMethodTracerを使用している場合、本対応を行なうとエラーになってしまう。
　　　java.lang.ExceptionInInitializerError　で回帰が1件も回らない
　　↓
　　↓対策
　　↓
　　testBeanRefContext.xml　の　<value>springconf/methodTracer.xml</value>　をコメントにする

-----------------------------------------------------------------------------------------------------------------
【close漏れの可能性がある場合の　出力例】
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:56) - Closeチェックエージェント起動
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:78) - モニタメソッド=null
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:79) - モニタメソッド(行)=null
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:80) - ターゲット=murata.ss.sx.sxb2015.editor.*
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:90) - JVM終了をフック
	2008-11-27 13:57:09,734 INFO  [main] (CloseCheckAgent.java:117) - 【静的処理】分析します。:murata.ss.sx.sxb2015.editor.Sxb2015EditorImpl
	2008-11-27 13:57:09,844 INFO  [main] (CloseCheckAgent.java:182) - 【静的処理】Executor生成の実装を検出：生成方法=prepareBatchInsert / ファイル=Sxb2015EditorImpl.java / 行数=231
	2008-11-27 13:57:09,969 INFO  [main] (CloseCheckAgent.java:164) - 【静的処理】Query生成の実装を検出：生成方法=query / ファイル=Sxb2015EditorImpl.java / 行数=255
	2008-11-27 13:57:09,985 INFO  [main] (CloseCheckAgent.java:164) - 【静的処理】Query生成の実装を検出：生成方法=queryForFill / ファイル=Sxb2015EditorImpl.java / 行数=283
	2008-11-27 13:57:10,000 INFO  [main] (CloseCheckAgent.java:164) - 【静的処理】Query生成の実装を検出：生成方法=queryForFill / ファイル=Sxb2015EditorImpl.java / 行数=342
	2008-11-27 13:57:10,000 INFO  [main] (CloseCheckAgent.java:164) - 【静的処理】Query生成の実装を検出：生成方法=queryForFill / ファイル=Sxb2015EditorImpl.java / 行数=382
	2008-11-27 13:57:10,000 INFO  [main] (CloseCheckAgent.java:224) - 【静的処理】拡張します。:murata.ss.sx.sxb2015.editor.Sxb2015EditorImpl
	2008-11-27 13:57:10,016 INFO  [main] (CloseCheckAgent.java:117) - 【静的処理】分析します。:murata.ss.sx.sxb2015.editor.Sxb2015Editor
	2008-11-27 13:57:10,016 INFO  [main] (CloseCheckAgent.java:224) - 【静的処理】拡張します。:murata.ss.sx.sxb2015.editor.Sxb2015Editor

	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:271) - ★★★CLOSE状態モニター（他のテストケースの結果も含みます）★★★
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=342
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - 疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382


読み方
	close()メソッドが呼ばれていないQuery/Cursor/Executorの生成位置を↓出力。

	疑わしい・・・[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283