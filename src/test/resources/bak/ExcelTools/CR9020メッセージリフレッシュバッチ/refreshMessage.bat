@echo off
setlocal enabledelayedexpansion
rem maven CR9020 Message Refresh バッチ
rem Ver1.0 CSK D.Saruhashi

rem 初期処理
set _EXEC_RESULT=0
set _LOGDIR=log\%DATE:/=%
set _BATCH_DIR=%~dp0
set _MESSAGE_FILE=%TEMP%\MESSAGE.dat
rem #### RMS DB ####
set _RMS_USR=schm_c200
set _RMS_PASS=schm_c200
set _RMS_SID=m0kbdb01
rem ################

rem 引数チェック
if "%1" == "" goto USAGE
if NOT "%1" == "dl" (
	if "%2" == "" goto USAGE
	if "%3" == "" goto USAGE
)

rem メイン
if "%1" == "dl" call :dl
if "%1" == "up" call :up %2 %3
if "%1" == "all" (
	call :dl
	call :up %2 %3
)

goto :END

rem dl
rem CR9020メッセージデータをファイルへダウンロードする
:dl

echo [!time:~0,8!][dl] message download from rms db...
sqlplus -s %_RMS_USR%/%_RMS_USR%@%_RMS_SID% @%_BATCH_DIR%messageDownload.sql "%_MESSAGE_FILE%"
echo [!time:~0,8!][dl] message download from rms db done.

exit /b

rem up
rem 指定スキーマへCR9020メッセージデータを登録する
:up

echo [!time:~0,8!][up] message uploading to %1.CR9020@%2...
sqlldr userid="%1/%1@%2" control="%_BATCH_DIR%CR9020.ctl" data="%_MESSAGE_FILE%" silent="all"
echo [!time:~0,8!][up] message upload to %1.CR9020@%2 done.

exit /b

rem USAGE
:USAGE

echo USAGE:
echo   refreshMassage [dl｜up｜all] [user] [SID]
echo.
echo   options
echo       dl    : message download from rms db to MESSAGE.dat
echo       up    : message upload from MESSAGE.dat to schema
echo       all   : dl ＆ up
echo.

rem 終了
:end

exit /b %_EXEC_RESULT%
endlocal
