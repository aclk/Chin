@ECHO OFF
REM ---------------------------------------
REM データを書き換えたためSVNとの差分を取りログを残す。
REM ---------------------------------------

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

REM ---ターゲット設定。引数があればそれらのプロダクト。なければフォルダ内すべて
SET TARGET=%*
IF "%*"=="" SET TARGET=%~dp0

REM ログのディレクトリを作成する。
IF NOT EXIST logs\svndiff_logs MKDIR logs\svndiff_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO SVNのDIFF
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		svn st %%D > %~dp0\logs\svndiff_logs\diff_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		) 
)
ENDLOCAL
