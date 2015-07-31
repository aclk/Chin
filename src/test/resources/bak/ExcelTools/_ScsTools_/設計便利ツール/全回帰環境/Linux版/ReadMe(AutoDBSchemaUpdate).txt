2010年9月24日 10:05:31 K.Miura@SCS

「SPIRITS自動DBスキーマ最新化スクリプト」

■What's this ?

	SPIRITSプロジェクトにおいて、日々更新されるプロダクト用ベースライン[murata-ps-baseline]の最新から、
	紐づく[murata-entitiy-baseline]を割り出し、そこからさらに紐付くEntity…から紐付くDDLを取得、
	指定したサーバ・スキーマに自動的に適用する、その機構とその道具・環境です。

■前提環境
	
	以下の環境を前提とします。
	
	・OS:Linux(一般的ディストリビューション、実際の動作確認環境は「OpenSuse」)
	※上記以外にも、以下に示す環境が揃っているUnix/Solaris/BSDなら、動作する可能性あり。
		
	・以下のソフトウェアが導入されている
		・UTF-8がデフォルトLocale
		・bashがデフォルトシェルである
		・svn(SPIRITSの標準SVNサーバに繋がっている状態)
		・sqlplus(対象サーバに接続可能、tnsnames.oraなどが整備された状態)
		・ruby
		・nkfコマンド

■用意するもの

	・このReadMe.txt
	・直下にあるファイル群
		spirits_dev_ps.tgz
		home_user.tgz
		crontab.txt

■構成
	
	[任意のディレクトリ]/spirits_dev_ps
		├scripts	← スクリプトの本体を格納。
		│└ringmail.rb	←メール送信用スクリプト。ruby製。
		├ddl	←実行ディレククトリ。スクリプト群は上記scriptsからのシンボリックリンク
		│├get_bl_ver_ddl.bsh
		│├get_latest_ddl.bsh
		│└all_ddl_reflash.bsh
		└logs	←実行時のログと使い終わったSQLの残骸を貯めるディレクトリ。
		
	[実行ユーザのhomeディレクトリ]
		└weekly_dbschema_update.bsh
	
	
	※スクリプト説明
	
		get_bl_ver_ddl.bsh
			引数に指定したmurata-entity-baselneのバージョンに紐づくDDL(無印、work、汎用T)を
			SVNコマンドで/tagからダウンロードし一所にまとめるスクリプト。
			
		get_latest_ddl.bsh
			murata-ps-baselineのsite内"プロジェクトの依存関係"ページから、
			murata-entity-baselineのバージョンを調べ、
			それを引数に上記get_bl_ver_ddl.bshを実行するスクリプト。
			
		all_ddl_reflash.bsh
			引数に指定されたsqlplus接続用引数(user/pass@tns)を使い、
			カレントディレクトリ内にある "murata-ddl*/ReplaceAllObjects.sql" を
			すべて連続で実行するスクリプト。
			
		weekly_dbschema_update.bsh
			get_latest_ddl.bsh、all_ddl_reflash.bsh を順番に実行し、完了後メールを送信するスクリプト。
			固定で複数のsqlplus接続用引数(user/pass@tns)、複数メールアドレスを指定可能。
			コピー・カスタマイズして使用する。
		
■導入方法

	①spirits_dev_ps.tgz を展開
		
		アーカイブを展開してください。
		任意のディレクトリにて、実行するユーザを用いて、
		  tar -xvpzf spirits_dev_ps.tgz
		で展開後、rootユーザで、/(ルートディレクトリ)へと移動させてください。
		
		※設置場所、ディレクトリ名を変更することは可能です。
		ただしその場合、後の設定ファイルの値変更時に変更する必要があります。


	②自動実行と結果のメール通知スクリプトを配置
		
		このReadMeと同じディレクトリ内にある、home_user.tgz を ~/ (homeディレクトリ)に展開します。

		  ※home_user.tgz を、~/ (homeディレクトリ)に置いたと仮定
		  tar -xvzf home_user.tgz
		
		weekly_dbschema_update.bsh というファイルが展開されると思います。
		ファイル名、内容を変更してください。
		
		  1.上部"TARGET_MAIL_ADDRESS"変数には、メールを送るアドレスを列挙してください。
		  	" "(半角スペース)、改行、Tabで区切り、複数指定することが可能です。
		  2.上部"MAIL_SUBJECT"変数には、メールのタイトルを指定してください。
		  3.上部"TARGET_SC"変数には、salplusコマンドの接続用引数(user/pass@tns)を列挙してください。
		  	" "(半角スペース)、改行、Tabで区切り、複数指定することが可能です。
		  4.下部"メール本文作成"コメント以下はメールの本文の内容です。お好みでカスタマイズしてください。
		  
		  複数パターン実行したい場合、コピー・リネームしそれぞれ別設定にしてください。
		
	③自動実行設定
		
		このReadMeと同じディレクトリ内にある、crontab.txt を参考に、
		crontab -e コマンドから実行設定を追加してください。
		(weekly_dbschema_update.bsh が含まれる行が必要な行です)
		※crontab.txtの例は、上記作業でファイル名を変更しない場合のものです。

■既知の問題

	・エラーを詳しく判定する機能が不足しています。
	　NGを通知してくれるのは「DDLが取れなかった時のみ」で、
	　「個々のDDL実行の失敗」は考慮しません。(OKで通知します)

