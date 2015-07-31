@echo off
setlocal enabledelayedexpansion
rem ファイル削除バッチ
rem ※一括削除時に「プロセスが握っていて～・・」エラーが頻繁に出て削除しきれない場合に活用
rem 
rem Ver1.0 CSK D.Saruhashi    新規作成

rem 初期設定
set _EXEC_RESULT=0
rem エラー時リトライ回数
rem set _MAX_DEL_CNT=100

rem 引数チェック
if "%1" == "" (
	set /P _DEL_PATH=削除対処PATHを入力してください：
) else (
	set _DEL_PATH=%1
)


rem 削除実行
:execdel
echo *** all delete start ***
rmdir /Q /S %_DEL_PATH% > nul
dir %_DEL_PATH% > nul
if errorlevel 1 (
	goto end
) else  (
	goto execdel
)
goto end

rem USAGE
:usage

echo usage: delAll [path]
echo.
set _EXEC_RESULT=1

goto end

rem 終了
:end
echo *** all delete done. ***
pause
set _LOGDIR=
exit /b %_EXEC_RESULT%

endlocal
