@echo off
REM 実行時間の取得(YYYYMMDDHHMM)
SET LOGTIME=%date%%time:~0,5%
SET LOGTIME=%LOGTIME:/=%
SET LOGTIME=%LOGTIME::=%
SET LOGTIME=%LOGTIME: =0%
IF NOT EXIST logs MKDIR logs
REM SET LOGFILE=logs\ExecuteToolPack%LOGTIME%.log
SET LOGFILE=logs\ExecuteToolPack.log

echo (実行ログは %LOGFILE% へ出力しています)
echo EntityTookPack実行開始 > %LOGFILE%
echo %date% %time% >> %LOGFILE%

SET /a START_TIME=%time:~0,2% * 60 + %time:~3,2%

REM ■定数・変数定義をインポート■
CALL VersionConstants.bat
CALL PCConstants.bat
CALL ParameterConstants.bat
SET _ERRMSG=""

echo REM ■SVNからエキスポート■ >> %LOGFILE%
echo SVNからエキスポート中...
CALL SvnExport.bat >> %LOGFILE%
IF ERRORLEVEL 1 (
  echo %LOGFILE%
  SET _ERRMSG="SVNからエキスポート エラー"
  GOTO ERROR
)
echo SVNからエキスポート完了

echo REM ■Entityスクリプト編集 >> %LOGFILE%
cscript EditEntityScript.vbs %DIR_ENTITY%\%SQL_REPLACEALLOBJECTS% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="EditEntityScript.vbs 実行 エラー"
  GOTO ERROR
)

echo REM ■不要なファイル(DBA用ファイル？)を削除 >> %LOGFILE%
REM (2009/08/18に作成されたファイルから登場。また変更になる可能性あり)
If EXIST %DIR_ENTITY%\CreateAllObjects.sql DEL %DIR_ENTITY%\CreateAllObjects.sql >> %LOGFILE%
If EXIST %DIR_WORK%\CreateAllObjects.sql DEL %DIR_WORK%\CreateAllObjects.sql >> %LOGFILE%
If EXIST %DIR_CORE%\CreateAllObjects.sql DEL %DIR_CORE%\CreateAllObjects.sql >> %LOGFILE%
If EXIST %DIR_MULTI%\CreateAllObjects.sql DEL %DIR_MULTI%\CreateAllObjects.sql >> %LOGFILE%

echo REM ■バージョン生成■ >> %LOGFILE%
echo バージョン生成中...
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_ENTITY% %VERSION_PREFIX_ENTITY% %TYPE_ENTITY% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs 実行 エラー"
  GOTO ERROR
)
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_CORE% %VERSION_PREFIX_CORE% %TYPE_CORE% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs 実行 エラー"
  GOTO ERROR
)
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_MULTI% %VERSION_PREFIX_MULTI% %TYPE_MULTI% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs 実行 エラー"
  GOTO ERROR
)
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_WORK% %VERSION_PREFIX_WORK% %TYPE_WORK% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs 実行 エラー"
  GOTO ERROR
)
echo バージョン生成完了

echo REM ■MultiのView化■ >> %LOGFILE%
echo MultiのView化中...
REM IF NOT %VERSION_MULTI%==%OLD_VERSION_MULTI% cscript MVtoView.vbs %EXCELTOOL_MV_FILEPATH% %DIR_MULTI% %VERSION_PREFIX_MULTI% %SQLFILEPATH_FORMULTI% %SQLFILEPATH_FORMULTI_ORGALL% >> %LOGFILE%
cscript MVtoView.vbs %EXCELTOOL_MV_FILEPATH% %DIR_MULTI% %VERSION_PREFIX_MULTI% %SQLFILEPATH_FORMULTI% %SQLFILEPATH_FORMULTI_ORGALL% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="MVtoView.vbs 実行 エラー"
  GOTO ERROR
)
echo MultiのView化完了

echo REM ■7z圧縮■ >> %LOGFILE%
echo 7z圧縮中...
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_ENTITY%.7z %DIR_ENTITY%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z圧縮 実行 エラー"
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_CORE%.7z %DIR_CORE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z圧縮 実行 エラー"
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_MULTI%.7z %DIR_MULTI%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z圧縮 実行 エラー"
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_WORK%.7z %DIR_WORK%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z圧縮 実行 エラー"
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_SDOLI%.7z %DIR_SDOLI%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z圧縮 実行 エラー"
  GOTO ERROR
)
echo 7z圧縮完了
REM EUCファイルのコピー
COPY %DIR_SMV1370%\SMV1370.sql %PACK_DIR%

echo REM ■差分生成(entityとworkのみ)■ >> %LOGFILE%
echo 差分生成中...
IF NOT %VERSION_ENTITY%==%OLD_VERSION_ENTITY% cscript Marge.vbs %EXCELTOOL_FILEPATH% %OLD_DIR_ENTITY% %DIR_ENTITY% %MARGEFILENAME% %VERSION_PREFIX_ENTITY% %TYPE_ENTITY% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Marge.vbs 実行 エラー"
  GOTO ERROR
)
IF NOT %VERSION_ENTITY%==%OLD_VERSION_ENTITY% MKDIR %DIR_MARGE%\%MARGE_DIR_PREFIX_ENTITY% >> %LOGFILE%
IF NOT %VERSION_ENTITY%==%OLD_VERSION_ENTITY% MOVE %EXCELTOOL_FILEDIR%\%MARGEFILENAME% %DIR_MARGE%\%MARGE_DIR_PREFIX_ENTITY% >> %LOGFILE%
IF NOT %VERSION_WORK%==%OLD_VERSION_WORK% cscript Marge.vbs %EXCELTOOL_FILEPATH% %OLD_DIR_WORK% %DIR_WORK% %MARGEFILENAME% %VERSION_PREFIX_WORK% %TYPE_WORK% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Marge.vbs 実行 エラー"
  GOTO ERROR
)
IF NOT %VERSION_WORK%==%OLD_VERSION_WORK% MKDIR %DIR_MARGE%\%MARGE_DIR_PREFIX_WORK% >> %LOGFILE%
IF NOT %VERSION_WORK%==%OLD_VERSION_WORK% MOVE %EXCELTOOL_FILEDIR%\%MARGEFILENAME% %DIR_MARGE%\%MARGE_DIR_PREFIX_WORK% >> %LOGFILE%
echo 差分生成完了

REM ■個別スクリプトのコピー
REM EUC(SMV1370)

REM ■Kドライブへ配置■
REM --処理未作成--

SET time=%time: =0%
SET /a END_TIME=%time:~0,2% * 60 + %time:~3,2%
SET /a EXECUTE_TIME=%END_TIME% - %START_TIME%

echo %date% %time% >> %LOGFILE%
echo 処理時間は %EXECUTE_TIME% 分です。 >> %LOGFILE%
echo 処理時間は %EXECUTE_TIME% 分です。

:ERROR
IF ERRORLEVEL 1 (
	ECHO %_ERRMSG%
	pause
)

@echo on
