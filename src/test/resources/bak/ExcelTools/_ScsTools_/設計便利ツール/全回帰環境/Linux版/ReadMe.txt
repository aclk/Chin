2010年7月23日 9:25:47 K.Miura@SCS

「SPIRITS全回帰テスト環境 - Linux版」

■What's this ?

	SPIRITSプロジェクトにおけるプログラムの最小単位「プロダクト」を、接続DBスキーマを固定して、
	任意数、自動実行でテストさせ続ける機構とその道具・環境です。

■前提環境
	
	以下の環境を前提とします。
	
	・OS:Linux(一般的ディストリビューション、実際の動作確認環境は「OpenSuse」)
	
	・UTF-8がデフォルトLocale
	・bashがデフォルトシェルである
	・SPIRITS標準環境(バッチサーバ環境に大体入っていたものです)
		・Java1.5以上
		・Maven2(SPIRITSの標準Mavenリポジトリに繋がっている状態)
		・svn(SPIRITSの標準SVNサーバに繋がっている状態)
	・ruby(SSのサーバ(竹重さんの忘れ形見)に入っていたので…)
	・nkfコマンド
	・JAVA_HOMEにjdkのパスが設定されている（jreではなく）

■用意するもの

	・このReadMe.txt直下にあるファイル群
	
■導入方法

	①spirits_dev_ss.tgz を展開
		
		アーカイブを展開してください。
		任意のディレクトリにて、回帰を主に実行するユーザを用いて、
		  tar -xvpzf spirits_dev_ss.tgz
		で展開後、rootユーザで、/(ルートディレクトリ)へと移動させてください。
		
		※設置場所、ディレクトリ名を変更することは可能です。
		ただしその場合、後の設定ファイルの値変更時に変更する必要があります。
		
		
	②mvnのrepository系設定変更
		
		各種環境依存の問題(文字コード、coverturaのバグ)などがあるため、
		mvnローカルリポジトリの設定を変更して対応します。
		
		・コンパイラの文字コード指定をはずす

			~/.m2/repository/murata/murata-parent/0.3.5/murata-parent-0.3.5.pom
		の        
			<artifactId>maven-compiler-plugin</artifactId>
		タグ内部から、encoding指定を削除
		 
		~/.m2/repository/murata/murata-app-parent/0.3.10/murata-app-parent-0.3.10.pom
		から、sjis決め打ちのところを、utf8に置換。
		同じく、ms932 を utf-8 に一括置換。計4箇所。

		・カバレッジがすべて100%になる対策

		バグの疑い濃厚。↓ここ参照
		http://d.hatena.ne.jp/taktos/20071101/1193891557
		これを参考に、
		~/.m2/repository/murata/murata-app-parent/0.3.10/murata-app-parent-0.3.10.pom
		に、足す。計三箇所。
		
		・prodact-testフレームワークの「機種依存問題」の解決
		
		プロダクトテストフレームワークの
		「内部パスの中に\(バックスラッシュ)が決め打ちで打たれている」問題の解決版、
		  murata-co-product-test-0.4.12(アーキ正式の対応…のクセにリポジトリに入れてくれない版)
		をローカルリポジトリに追加する。
		
		※murata-co-product-test-0.4.12.tgz を、~/ (homeディレクトリ)に置いたと仮定
		cd ~/.m2/repository/murata/murata-co-product-test
		mv ~/murata-co-product-test-0.4.12.tgz ./
		tar -xvzf murata-co-product-test-0.4.12.tgz
		
		
	③回帰環境作成
		
		・DB接続・Logの設定
		
		①で展開した、spirits_dev_ss の reg_daily ディレクトリを任意の名前でコピーし、
		その中 tools/SAITO にある
		  applicationContext-datasource-local.xml
		  jdbc.properties
		  log4j.xml
		を、実行したい環境へと書き換える。
		
		・対象プロダクト指定
		
		上記、コピーしたディレクトリ内の
		
		  testee.txt
		  
		に、実行したいプロダクトID(SVNにおけるプロジェクト名)を列挙する。
		
		・これで導入完了です。テストとして ./00.full.sh を実行し、
		出力される trt-result.txt を確認してください。


	☆番外-自動実行と結果のメール通知機能の付加
		
		※必須ではありません。必要でない方は無視してください。
		※メール通知スクリプトを実行するにはRubyとRuby-gemが必要です。入れられない環境の方は諦めて下さい。
		※メール通知スクリプトは試験的に提供するものです。起こる事故にSCSは一切の責任を負いません。
		
		・メール通知スクリプト設置
		
		このReadMe.txtと同じディレクトリ内にある、home_user.tgz を ./ (homeディレクトリ)に展開します。

		  ※home_user.tgz を、~/ (homeディレクトリ)に置いたと仮定
		  tar -xvzf home_user.tgz
		
		ss_daily_regression_kick.bsh というファイルが展開されると思います。
		ファイル名、内容を変更してください。
		
		  1.上部"TARGET_MAIL_ADDRESS"変数には、メールを送るアドレスを列挙してください。
		  2.上部"MAIL_SUBJECT"変数には、メールのタイトルを指定してください。
		  3.上部"TARGET"変数には、上記①～③で作成した環境のPathを指定してください。
		  4.下部"メール本文作成"コメント以下はメールの本文の内容です。お好みでカスタマイズしてください。
		
		・自動実行設定
		
		このReadMe.txtと同じディレクトリ内にある、crontab.txt を参考に、
		crontab -e コマンドから実行設定を追加してください。
		※crontab.txtの例は、上記作業でファイル名を変更しない場合のものです。

■既知の問題

	・プロダクト側からメッセージリソース(CR9020)を変更するようなテストデータがある場合、
	その次に実行されるプロダクトから結果がグチャグチャになります。
	※変更なされないことを期待している上、メッセージリソースを取りに行くDB接続の書き換えに対応していないため。

■Tips

	・ベースラインを違えて実行したい場合
		linuxユーザを変更して、ローカルリポジトリの murata-app-parentのpomをいじってください。
		ただし、実行環境ファイル群のパーミションは、変更したユーザで実行できるよう変更しておくこと。
		
	・プロダクト毎のテストから操作を追加・削除したい場合
		./00.full.sh を変更してください。
		ただしデフォルトでスクリプトはすべてシンボリックリンクのため、コピー→変更してください。

■構想(9月に解雇されるため、たぶん未完…)

	・既知の問題の解決。(メッセージリソースのDB接続も書き換えるように)
	・結果収集を自動＆DB化、より詳細な「日々の変化点」をレポートするよう機能追加。
	・同じ仕組みを使って「ワンコマンドでリリースできる仕組み」
	・同じ仕組みを使って「ワンコマンドでSVNタグを切る仕組み」