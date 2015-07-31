■SummaryChecker

　□なにをするツール？
　　- ProjectSiteのSurefireReportのsummaryを取得するツール
　
　□なにが便利？
　　- プロダクトを複数指定すれば回帰結果の一覧表作成が可能
　
　□使い方
　　１．ProjectをEclipseへインポートする
　　　ファイル　＞　インポート　＞　既存プロジェクトをワークスペースへ
　　　
　　　ルートディレクトリにSummaryCheckerが配置されているディレクトリを指定して
　　　SummaryCheckerをインポートし、ビルドする
　　　
　　２．対象プロダクトの設定
　　　target.txtに対象プロダクトのURLを列挙する
　　
　　３．実行
　　　main/SummaryChecker を右クリックし、　実行　＞　Javaアプリケーション　で起動


　□出力サンプル（※タブ区切りなのでExcelにそのまま貼り付けると表になります）
　
ProductID	Tests	Errors	Failures	Skipped	Success Rate	Time
murata-ss-bsp	664	0	0	0	100%	2,634.109
murata-ss-sj-bsp	88	0	0	0	100%	139.361
murata-sdb0010	7	0	0	0	100%	40.255
murata-sdb0020	4	0	0	0	100%	77.623
murata-sdb0030	8	0	0	0	100%	191.254
