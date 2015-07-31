■改定履歴
1.0     2009/08/5   川床    新規作成
1.1     2009/08/7   川床    Entityの中にあるMVをテスト用(ON COMMIT)のDDLを適用するように変更する処理を追加
1.2     2009/08/20  川床    不要なファイル(DBA用ファイル？)を削除する処理を追加
1.3     2009/10/12  川床    MVの更新属性をON DEMAND→ON COMMITへ変換する処理を追加
1.4     2009/12/18  川床    SVNから取得時のロジックを修正
                                新パスから取得。取得できない場合は旧パスから取得。
                                （旧：http://m6f1b013/svn/prod/tags/ ⇒ 新：http://m6f1b013/svn/prod/spirits/co/tags/）
                            【MultiのView化】の処理をバージョン変更無しの場合も実行するように修正
1.5     2010/03/12  川床    1.SDOLIスクリプトを対象に追加
                            2.SJO3600性能改善MVのSMV1370(EUC)を対象に追加
                            3.Triggerスクリプトを実行しないようにEntityのReplaceAllObjects.sqlを補正
                            4.部品表関連MVスクリプトを実行しないように ReplaceAllObjects.sqlを補正
                            5.圧縮ファイル形式を7z 形式に固定
                            6.Constantsファイルを整備(VersionConstants,PCConstants,ParameterConstants)

■事前準備
・「Entityスクリプト生成ツール.xls」の最新版を取得
・「MV→View変換マクロ.xls」の最新版を取得

・SVNをインストール

■EntityTookPackの実行手順
・VersionConstants.bat の内容を修正
・PCConstants.bat の内容を修正

・All.bat を実行

    VersionConstants.bat で設定したバージョンを、
    PCConstants.bat で設定した DDL_DIR の場所へ作成します。
    「ｸﾞﾛｰﾊﾞﾙﾍﾞｰｽﾗｲﾝﾊﾞｰｼﾞｮﾝ」フォルダが作成され、その下にDDLが作成されます。
    中をチェックし、OKであれば「ｸﾞﾛｰﾊﾞﾙﾍﾞｰｽﾗｲﾝﾊﾞｰｼﾞｮﾝ」フォルダを丸々Kドライブにコピーして完了。
    
■実行時間の目安
Core以外の新・旧両方取得して20分程

■ログファイルについて
「logs\ExecuteToolPack.log」に毎回出力しています。
履歴として残したい場合は、「All.bat」の以下の箇所を切り替えてください。

------------------------------------------
REM SET LOGFILE=logs\ExecuteToolPack%LOGTIME%.log
SET LOGFILE=logs\ExecuteToolPack.log
------------------------------------------
