@ECHO OFF
REM ---------------------------------------
REM 確認のための再テスト実行
REM ---------------------------------------

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

REM ---ターゲット設定。引数があればそれらのプロダクト。なければフォルダ内すべて
SET TARGET=%*
IF "%*"=="" SET TARGET=%~dp0

REM ログのディレクトリを作成する。
IF NOT EXIST logs\confirmtest_logs MKDIR logs\confirmtest_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO 確認のための再テスト
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		CD %%~nD
		SET PROD_ID=%%~nD
		mvn clean site > %~dp0\logs\confirmtest_logs\test_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~1,1%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		CD %~dp0) 
)
ENDLOCAL
