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
IF NOT EXIST logs\rdcreate_logs MKDIR logs\rdcreate_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO 回帰データ作成。
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		CD %%~nD
		ECHO %%DをTestモジュール込みでコンパイル。Target内に設定ファイルを作り出し、それを元に回帰データ作成。
		mvn clean test-compile rdconv:convert > %~dp0\logs\rdcreate_logs\rdcreate_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~1,1%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		REM 一階層上がる
		CD %~dp0
		) 
)
ENDLOCAL
