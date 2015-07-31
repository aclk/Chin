@echo off
setlocal enabledelayedexpansion
rem transientチェックバッチ
rem Ver1.0 CSK D.Saruhashi
rem 

rem 初期処理
rem #### パスワード変更時、ホスト変更時は要メンテ #####
rem バッチサーバ
rem -- m0tabt
set _BT_USER=rscsk
set _BT_PASS=xxxx
set _BT_HOST=m0tabt
rem -- m0iabt
set _BT_MSZ_USER=rscsk
set _BT_MSZ_PASS=xxxx
set _BT_MSZ_HOST=m0iabt
rem #####################################
set _FTP_CMD_FILE=_ftp_cmd.txt
set _TIME=%time: =0%
set _LOGDIR=log\%DATE:/=%
set _LOGFILE=%_LOGDIR%\checkTransient_%DATE:/=%_%_TIME:~0,2%%_TIME:~3,2%%_TIME:~6,2%.log
set _EXEC_RESULT=0

if not exist %_LOGDIR%\nul (mkdir %_LOGDIR%)

rem 実行
:exec

rem m0tabt
echo open %_BT_HOST%> %_FTP_CMD_FILE%
echo %_BT_USER%>> %_FTP_CMD_FILE%
echo %_BT_PASS%>> %_FTP_CMD_FILE%
echo prompt>> %_FTP_CMD_FILE%

rem echo **** PSZ spirits100 transient lib ****
echo cd /murata/spirits100/lib/transient>> %_FTP_CMD_FILE%
echo ls -l>> %_FTP_CMD_FILE%

rem rem echo **** PSZ spirits200 transient lib ****
rem echo cd /murata/spirits200/lib/transient>> %_FTP_CMD_FILE%
rem echo ls -l>> %_FTP_CMD_FILE%
rem 
rem rem echo **** PSZ spirits300 transient lib ****
rem echo cd /murata/spirits300/lib/transient>> %_FTP_CMD_FILE%
rem echo ls -l>> %_FTP_CMD_FILE%

rem echo bye>> %_FTP_CMD_FILE%
echo close>> %_FTP_CMD_FILE%

rem m0iabt
echo open %_BT_MSZ_HOST%>> %_FTP_CMD_FILE%
echo %_BT_MSZ_USER%>> %_FTP_CMD_FILE%
echo %_BT_MSZ_PASS%>> %_FTP_CMD_FILE%
echo prompt>> %_FTP_CMD_FILE%

rem echo **** MSZ spirits200 transient lib ****
echo cd /murata/spirits200/lib/transient>> %_FTP_CMD_FILE%
echo ls -l>> %_FTP_CMD_FILE%

echo bye>> %_FTP_CMD_FILE%
echo close>> %_FTP_CMD_FILE%


ftp -s:%_FTP_CMD_FILE%>%_LOGFILE%

del %_FTP_CMD_FILE%

more %_LOGFILE%

goto :end

rem USAGE
:usage

echo usage: checkTransient.bat
echo.
set _EXEC_RESULT=1

rem 終了
:end

exit /b %_EXEC_RESULT%
endlocal
