@ECHO OFF

ECHO 回帰データの再作成を行います。
ECHO 時間がかかることが予想されます。ノンストップなので嫌な人はここで引き返すべしです。

CALL 03.設定書換とコピー.bat
CALL 04.回帰データからテストデータファイルへコピー.bat
CALL 05.テストのみ.bat
CALL 06.回帰データ作成.bat
CALL 07.設定元へ戻す.bat
CALL 08.確認のため再テスト.bat
CALL 09.SVNとの差分取得.bat

ECHO 回帰データの再作成が終了しました。

PAUSE
