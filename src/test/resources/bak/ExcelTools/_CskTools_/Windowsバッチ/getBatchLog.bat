@echo off
setlocal enabledelayedexpansion
rem バッチログ取得用バッチ
rem Ver1.0 CSK D.Saruhashi

rem 初期処理
rem #### パスワード変更時、ホスト変更時は要メンテ #####
set _USER=rscsk
set _PASS=xxxx
set _BATCH_HOST=m0tabt
rem #####################################
set _FTP_CMD_FILE=_ftp_cmd.txt
set _EXEC_RESULT=0

rem 引数チェック
if "%1" == "" goto usage
if "%2" == "" goto usage
if "%3" == "" goto usage

rem ログ取得
:execGetLog

echo open %_BATCH_HOST%> %_FTP_CMD_FILE%
echo %_USER%>> %_FTP_CMD_FILE%
echo %_PASS%>> %_FTP_CMD_FILE%
echo cd /murata/spirits%1/log/%2>> %_FTP_CMD_FILE%
echo get %3-std.log>> %_FTP_CMD_FILE%
echo get %3-err.log>> %_FTP_CMD_FILE%
echo quit>> %_FTP_CMD_FILE%

ftp -s:_ftp_cmd.txt

del %_FTP_CMD_FILE%

goto :end

rem USAGE
:usage

echo usage: getBatchLog [home dir spirits number] [subsystem name] [JOB name]
echo.
echo ex) getBatchLog 100 ss JSJD734
echo.
set _EXEC_RESULT=1

rem 終了
:end

exit /b %_EXEC_RESULT%
endlocal