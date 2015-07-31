@echo off
setlocal enabledelayedexpansion
rem maven close忘れチェックバッチ
rem Ver1.0 CSK D.Saruhashi

rem 初期処理
set _DB_SETTING_JAR_PATH=%~dp0\DB設定書き換え
set _LOGDIR=log\%DATE:/=%
set _DB_SCHEMA=sys1
set _PROCESS_TYPE=closeCheck

rem set _PROCESS_TYPE=test
rem set _RESULT_FILE=%_LOGDIR%\%DATE:/=%_test_%_DB_SCHEMA%.log
set _EXEC_RESULT=0
set _T1=!TIME:~0,8!
set _T2=!TIME:~0,8!
if not exist %_LOGDIR%\nul (mkdir %_LOGDIR%)

rem 引数チェック
if "%1" == "" goto usage

rem 回帰テスト実行
:exectest
if "%1" == "" goto end
if exist %1\nul (

set _T1=!TIME:~0,8!

rem スキーマ変更
echo [!time:~0,8!][%1] dataSouce [%_DB_SCHEMA%] Change. 
java -jar %_DB_SETTING_JAR_PATH%\DBSetting.jar %1 0 %_DB_SCHEMA%

cd %1

rem close check 準備
echo [!time:~0,8!][%1] close check 準備中...
rem copy %~dp0\CloseCheckAspect.java src\test\java\ > nul
rem copy src\test\resources\springconf\applicationContext-product-test.xml src\test\resources\springconf\applicationContext-product-test.xml.bak > nul
copy pom.xml pom.xml.bak > nul
cscript %~dp0\addPointcut.vbs %~dp0 %1 > nul
if exist src\test\resources\log4j.xml (
	copy src\test\resources\log4j.xml src\test\resources\log4j.xml.bak > nul
	copy %~dp0\log4j.sample.xml src\test\resources\log4j.xml > nul
)
if exist src\main\resources\log4j.xml (
	copy src\main\resources\log4j.xml src\main\resources\log4j.xml.bak > nul
	copy %~dp0\log4j.sample.xml src\main\resources\log4j.xml > nul
)

echo [!time:~0,8!][%1] close check 実施中...
echo ☆★☆★☆★☆★ [!date! !time!][%1] close check start ☆★☆★☆★☆★ >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
call monitorTime.bat mvn clean test >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
copy CloseCheck.log %1-CloseCheck.log > nul
del CloseCheck.log
find /C "疑わしい" %1-CloseCheck.log
call cleanupForCloseCheck
echo ☆★☆★☆★☆★ [!date! !time!][%1] close check end   ☆★☆★☆★☆★ >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
echo [!time:~0,8!][%1] close check done...
cd ..
) else echo [!time:~0,8!] "%1" プロジェクトが存在しません.
shift /1
goto exectest

rem USAGE
:usage

echo usage: closeCheck [productId] [productId] ...
echo.
set _EXEC_RESULT=1

goto end

rem 終了
:end
set /P x=total elapsed：<NUL
call calcTime %_T2% !TIME:~0,8!
exit /b %_EXEC_RESULT%
endlocal
