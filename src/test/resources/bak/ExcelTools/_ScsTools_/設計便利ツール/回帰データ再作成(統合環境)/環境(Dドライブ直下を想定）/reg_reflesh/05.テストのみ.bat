@ECHO OFF
REM ---------------------------------------
REM エビデンス出力のためのテスト実行
REM ---------------------------------------

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

REM ---ターゲット設定。引数があればそれらのプロダクト。なければフォルダ内すべて
SET TARGET=%*
IF "%*"=="" SET TARGET=%~dp0

REM ログのディレクトリを作成する。
IF NOT EXIST logs\autotest_logs MKDIR logs\autotest_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO テスト実施
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		CD %%~nD
		SET PROD_ID=%%~nD
		mvn clean test > %~dp0\logs\autotest_logs\test_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~1,1%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		CD %~dp0) 
)
ENDLOCAL
