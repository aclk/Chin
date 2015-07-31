★CommitterTooles

by CSK D.Saruhashi

■事前設定
　- Subversionをインストールしておく
　- committerToolesへのPATHを通しておく
　- 利用スキーマ名をsetSchemaEnv.batバッチ中の「_DB_SCHEMA」に設定する
　- SVNアカウントをsetSVNEnv.batバッチ内の「_SVN_USER」に設定する

■一括処理イメージ
　- カレントディレクトリ直下にプロダクトをチェックアウトする
　- 直下にチェックアウトしたプロダクト内で指定したプロダクトに対し処理を実施する
　- maven test、maven deployを実行するバッチに関しては処理前にデータソース書き換えが実行される

■コマンド引数について
　第一引数で {option} を指定した場合、SVN取得元が以下の通り変更される
　
　　sstrunk ：spirits/ss/trunk
　　pstrunk ：spirits/ps/trunk
　　mstrunk ：spirits/ms/trunk
　　dbmtrunk：spirits/dbm/trunk
　　bmctrunk：spirits/bmc/trunk
　　
　　sszn    ：spirits/ss/branches/zn
　　pszn    ：spirits/ps/branches/zn
　　mszn    ：spirits/ms/branches/zn
　　dbmzn   ：spirits/dbm/branches/zn
　　
　　swat    ：branches/swat_csk
　　mssys   ：branches/bmc-systemtest-sysm
　　msrun   ：spirits/ms/branches/running

■checkout
　> svnCheckOut {option} [productID] [productID] ...

■FileCheck（pom.xml、index.apt）
　> checkFiles [productID] [productID] ...

■test
　> mvntest [productID] [productID] ...

■commands
　> mvndeploy                        [productID] [productID] ...
　> mvndeployForCI                   [productID] [productID] ... （BTP-CI対象プロダクトの場合こちらを使用する）
　> mvndeploy_NoTest                 [productID] [productID] ... （no testでdeployする場合）
　> mvndeployForCI_NoTest            [productID] [productID] ... （no testでdeployする場合(BTP-CI対象)）
　> mvndeploy_NoTestWithSite         [productID] [productID] ... （no testでdeploy及びsiteをuploadする）
　> mvndeployForCI_NoTestWithSite    [productID] [productID] ... （no testでdeploy及びsiteをuploadする(BTP-CI対象)）
　> mvnsite                          [productID] [productID] ... （siteをuploadする）
　> mvnsite_NoTest                   [productID] [productID] ... （no testでsiteをuploadする）
　> mvnsite_NoDeploy                 [productID] [productID] ... （siteを作成（uploadはしない））
　> mvnsite_ForCov                   [productID] [productID] ... （debug logレベルにしsiteを作成（uploadはしない））
　> mvnCheckoutSite                  [productID] [productID] ... （svn checkout & mvn site）
　
──[OPTION]──────────────────────────────────

■SVN update（配下の全プロジェクトに対してsvn updateを実行）
　> allUpdate

■SVN add tag
　> svnAddTag {option} [productID] [version]

■SVN check tag（tag付けされているかチェックする）
　> svnCheckTag {option} [productID] [version]

■SVN delete tag（tag付けを削除する）
　> svnDelTag {option} [productID] [version]
　
■truncaheSchema（指定したスキーマの全テーブルをtruncateする(除：CR9020、DB_VERSION)）
　> truncaheSchema [user] [schema]

──[HISTORY]──────────────────────────────────
2010/06/16 1.20 svnAddTag修正、mvnsite_ForCov追加
2010/03/08 1.19 mvnsite_NoDeploy、mvnCheckoutSite追加
2010/02/18 1.18 svnDelTag追加、svnAddTagは事前にdeleteしてからtag付けするよう変更
2009/12/04 1.17 SVN構成変更に対応
2009/07/21 1.16 bmctrunkに対応
2009/07/17 1.15 swat_csk branchに対応
2009/07/16 1.14 mvndeploy_NoTestWithSite mvndeployForCI_NoTestWithSite 追加
2009/07/15 1.13 mvnsite_NoTest 追加
2009/04/02 1.12 SVN checkout再実行処理が正しく動くよう修正
2009/03/13 1.11 svnAddTagのsvn mkdirに「--parents」オプションを追加
                mvnsiteコマンドを追加
2009/03/11 1.10 SVN構成変更に対応（msrun、mstrunk対応）
2009/02/25 1.00 リリース


──[SPECIAL THANKS]───────────────────────────────
DB設定書き換えツール　作成者：CSK nimota
