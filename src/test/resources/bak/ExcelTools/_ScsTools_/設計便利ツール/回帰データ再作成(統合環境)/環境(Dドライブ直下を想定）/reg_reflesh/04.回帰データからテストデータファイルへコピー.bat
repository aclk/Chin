ECHO OFF

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

SET TARGET=%*

REM ---スキーマ、ベースライン、バージョン設定。呼び元から与えられていればそれを利用。
IF "%BASEDIR%"  =="" SET BASEDIR=D:\evidence
IF "%INPUT%"    =="" SET INPUT=input
IF "%OUTPUT%"   =="" SET OUTPUT=output


SET INPUTDIR=


:COPY_AUTOTESTDATA
ECHO.
ECHO ---------------------------------------
ECHO 回帰データファイルのコピー
ECHO ---------------------------------------
ECHO.

ECHO 元のワークフォルダを空にする
rmdir /S /Q %BASEDIR%
mkdir %BASEDIR%

ECHO プロダクトごとのフォルダを作成、データをコピー
ECHO %TARGET%
SET START_DIR=%~dp0
FOR /D %%D IN (%TARGET%*) DO (
    REM フォルダを移動しまくるため、ループの最初で元へ戻る
    CD %START_DIR%
    echo %%D\pom.xml
    IF EXIST %%D\pom.xml (
        CD %%D\src\test
        ECHO フォルダを作成
        mkdir %BASEDIR%\%%D\%INPUT%
        mkdir %BASEDIR%\%%D\%OUTPUT%
        ECHO 回帰データをコピー
        FOR /F "usebackq" %%I IN (`DIR /S /B TR_*.xls`) DO (
            xcopy %%I %BASEDIR%\%%D\%INPUT%
        )
        ECHO TR_ → TP_ にリネーム
        CD %BASEDIR%\%%D\%INPUT%
        RENAME TR_* TP_*
    )
)


:END
ENDLOCAL
