@echo off
setlocal enabledelayedexpansion
rem モジュールアップローダー for バッチ
rem Ver1.0 CSK D.Saruhashi
rem 
rem ※PuTTYに同梱しているpsftpへのパスを設定てしておくこと

rem 初期処理
rem #### パスワード変更時、ホスト変更時は要メンテ #####
rem upload先ホスト
rem -- m0tabt
set _BT_USER=rscsk
set _BT_PASS=xxxx
set _BT_HOST=m0tabt
rem -- m0iabt
set _BT_MSZ_USER=rscsk
set _BT_MSZ_PASS=xxxx
set _BT_MSZ_HOST=m0iabt
rem -- SSCSK事前確認バッチ環境：m0tubt20
set _SSCSK_USER=rsvf
set _SSCSK_PASS=xxxx
set _SSCSK_BATCH_HOST=m0tubt20
rem -- SSCSK事前確認オンライン環境：m0tuwb10
set _SSCSK_OL_USER=wls09
set _SSCSK_OL_PASS=wls09
set _SSCSK_OL_HOST=m0tuwb10
rem -- DBM事前確認バッチ環境：m0tubt10
set _DBMCSK_USER=rsvf
set _DBMCSK_PASS=xxxx
set _DBMCSK_BATCH_HOST=m0tubt10
rem maven host
set _MVN_USER=maven
set _MVN_PASS=maven
set _MVN_HOST=m6f1b012
rem #####################################
set _TMP_DIR=%TEMP%
set _FTP_CMD_FILE=_ftp_cmd.txt
set _LOGDIR=log\%DATE:/=%
set _LOG_FILE=%_LOGDIR%\uploadModule.log
set _EXEC_RESULT=0
if not exist %_LOGDIR%\nul (mkdir %_LOGDIR%)

rem 引数チェック
rem %1:リリース先 nas/100/200/300/03
rem %2:productId
rem %3:productVer
if "%1" == "" goto usage
if "%2" == "" goto usage
if "%3" == "" goto usage

rem モジュール名
set _ARG2=%2
if "%_ARG2:~0,7%" == "murata-" (
	set _MOD_NAME=%2
) else (
	set _MOD_NAME=murata-%2
)

rem モジュールアップロードメイン
:execUploadModule

echo ★★upload To[%1] module[%2] 誤りであればCTRL-Cで本バッチを停止して下さい!!★★
pause

echo [!time:~0,8!] uploadModule start...

pushd %_TMP_DIR%
rem maven download
echo **** maven download ****
echo cd /mnt/maven/repository/murata/%_MOD_NAME%/%3> %_FTP_CMD_FILE%
echo get %_MOD_NAME%-%3.jar>> %_FTP_CMD_FILE%
echo quit>> %_FTP_CMD_FILE%

psftp %_MVN_USER%@%_MVN_HOST% -pw %_MVN_PASS% -b _ftp_cmd.txt

del %_FTP_CMD_FILE%
echo.
rem module upload
echo **** module upload ****
if "%1" == "nas" (
	echo cd /nas/app/deploywk/batch/murata_psz/spirits/lib/transient/> %_FTP_CMD_FILE%
) else if "%1" == "sscsk" (
	echo cd /spirits_b/ss/sscsk/lib/> %_FTP_CMD_FILE%
) else if "%1" == "sscsk_online" (
	echo cd /opt/bea/user_projects/domains/stage_domain/servers/wlstage9/stage/spirits-sscsk/spirits/WEB-INF/lib/> %_FTP_CMD_FILE%
) else if "%1" == "sscsk_remote" (
	echo cd /opt/bea/user_projects/domains/stage_domain/servers/wlstage9/stage/btpremote-sscsk/btpremote/WEB-INF/lib/> %_FTP_CMD_FILE%
) else if "%1" == "dbmcsk" (
	echo cd /spirits_b/dbm/dbmcsk/lib/> %_FTP_CMD_FILE%
) else if "%1" == "msz" (
	echo cd /murata/spirits200/lib/transient> %_FTP_CMD_FILE%
) else (
	echo cd /murata/spirits%1/lib/transient> %_FTP_CMD_FILE%
)
echo put %_MOD_NAME%-%3.jar>> %_FTP_CMD_FILE%
echo ls %_MOD_NAME%*>> %_FTP_CMD_FILE%
echo quit>> %_FTP_CMD_FILE%

if "%1" == "sscsk" (
	psftp %_SSCSK_USER%@%_SSCSK_BATCH_HOST% -pw %_SSCSK_PASS% -b %_FTP_CMD_FILE%
) else if "%1" == "sscsk_online" (
	psftp %_SSCSK_OL_USER%@%_SSCSK_OL_HOST% -pw %_SSCSK_OL_PASS% -b %_FTP_CMD_FILE%
) else if "%1" == "sscsk_remote" (
	psftp %_SSCSK_OL_USER%@%_SSCSK_OL_HOST% -pw %_SSCSK_OL_PASS% -b %_FTP_CMD_FILE%
) else if "%1" == "dbmcsk" (
	psftp %_DBMCSK_USER%@%_DBMCSK_BATCH_HOST% -pw %_DBMCSK_PASS% -b %_FTP_CMD_FILE%
) else if "%1" == "msz" (
 	psftp %_BT_MSZ_USER%@%_BT_MSZ_HOST% -pw %_BT_MSZ_PASS% -b %_FTP_CMD_FILE%
) else (
 	psftp %_BT_USER%@%_BT_HOST% -pw %_BT_PASS% -b %_FTP_CMD_FILE%
)

del %_FTP_CMD_FILE%
del %_MOD_NAME%-%3.jar

popd

echo [!time:~0,8!] uploadModule uploaded.
echo [!time:~0,8!][%2] Ver.%3 %1 uploaded.>>%_LOG_FILE%

goto :end

rem USAGE
:usage

echo usage: uploadModule ["nas" or "sscsk" or spirits Number or "msz" or "sscsk_online" or "sscsk_remote" or "dbmcsk"] [productId] [productVer]
echo.
echo ex) uploadModule sscsk sjo0500 1.0.0
echo.
set _EXEC_RESULT=1

rem 終了
:end

exit /b %_EXEC_RESULT%
endlocal
