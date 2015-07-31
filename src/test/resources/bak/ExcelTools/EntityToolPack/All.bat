@echo off
REM ���s���Ԃ̎擾(YYYYMMDDHHMM)
SET LOGTIME=%date%%time:~0,5%
SET LOGTIME=%LOGTIME:/=%
SET LOGTIME=%LOGTIME::=%
SET LOGTIME=%LOGTIME: =0%
IF NOT EXIST logs MKDIR logs
REM SET LOGFILE=logs\ExecuteToolPack%LOGTIME%.log
SET LOGFILE=logs\ExecuteToolPack.log

echo (���s���O�� %LOGFILE% �֏o�͂��Ă��܂�)
echo EntityTookPack���s�J�n > %LOGFILE%
echo %date% %time% >> %LOGFILE%

SET /a START_TIME=%time:~0,2% * 60 + %time:~3,2%

REM ���萔�E�ϐ���`���C���|�[�g��
CALL VersionConstants.bat
CALL PCConstants.bat
CALL ParameterConstants.bat
SET _ERRMSG=""

echo REM ��SVN����G�L�X�|�[�g�� >> %LOGFILE%
echo SVN����G�L�X�|�[�g��...
CALL SvnExport.bat >> %LOGFILE%
IF ERRORLEVEL 1 (
  echo %LOGFILE%
  SET _ERRMSG="SVN����G�L�X�|�[�g �G���["
  GOTO ERROR
)
echo SVN����G�L�X�|�[�g����

echo REM ��Entity�X�N���v�g�ҏW >> %LOGFILE%
cscript EditEntityScript.vbs %DIR_ENTITY%\%SQL_REPLACEALLOBJECTS% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="EditEntityScript.vbs ���s �G���["
  GOTO ERROR
)

echo REM ���s�v�ȃt�@�C��(DBA�p�t�@�C���H)���폜 >> %LOGFILE%
REM (2009/08/18�ɍ쐬���ꂽ�t�@�C������o��B�܂��ύX�ɂȂ�\������)
If EXIST %DIR_ENTITY%\CreateAllObjects.sql DEL %DIR_ENTITY%\CreateAllObjects.sql >> %LOGFILE%
If EXIST %DIR_WORK%\CreateAllObjects.sql DEL %DIR_WORK%\CreateAllObjects.sql >> %LOGFILE%
If EXIST %DIR_CORE%\CreateAllObjects.sql DEL %DIR_CORE%\CreateAllObjects.sql >> %LOGFILE%
If EXIST %DIR_MULTI%\CreateAllObjects.sql DEL %DIR_MULTI%\CreateAllObjects.sql >> %LOGFILE%

echo REM ���o�[�W���������� >> %LOGFILE%
echo �o�[�W����������...
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_ENTITY% %VERSION_PREFIX_ENTITY% %TYPE_ENTITY% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs ���s �G���["
  GOTO ERROR
)
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_CORE% %VERSION_PREFIX_CORE% %TYPE_CORE% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs ���s �G���["
  GOTO ERROR
)
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_MULTI% %VERSION_PREFIX_MULTI% %TYPE_MULTI% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs ���s �G���["
  GOTO ERROR
)
cscript Version.vbs %EXCELTOOL_FILEPATH% %DIR_WORK% %VERSION_PREFIX_WORK% %TYPE_WORK% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Version.vbs ���s �G���["
  GOTO ERROR
)
echo �o�[�W������������

echo REM ��Multi��View���� >> %LOGFILE%
echo Multi��View����...
REM IF NOT %VERSION_MULTI%==%OLD_VERSION_MULTI% cscript MVtoView.vbs %EXCELTOOL_MV_FILEPATH% %DIR_MULTI% %VERSION_PREFIX_MULTI% %SQLFILEPATH_FORMULTI% %SQLFILEPATH_FORMULTI_ORGALL% >> %LOGFILE%
cscript MVtoView.vbs %EXCELTOOL_MV_FILEPATH% %DIR_MULTI% %VERSION_PREFIX_MULTI% %SQLFILEPATH_FORMULTI% %SQLFILEPATH_FORMULTI_ORGALL% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="MVtoView.vbs ���s �G���["
  GOTO ERROR
)
echo Multi��View������

echo REM ��7z���k�� >> %LOGFILE%
echo 7z���k��...
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_ENTITY%.7z %DIR_ENTITY%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z���k ���s �G���["
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_CORE%.7z %DIR_CORE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z���k ���s �G���["
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_MULTI%.7z %DIR_MULTI%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z���k ���s �G���["
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_WORK%.7z %DIR_WORK%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z���k ���s �G���["
  GOTO ERROR
)
%ZIP_PATH% a -t7z %PACK_DIR%\%DIR_PREFIX_SDOLI%.7z %DIR_SDOLI%
IF ERRORLEVEL 1 (
  SET _ERRMSG="7z���k ���s �G���["
  GOTO ERROR
)
echo 7z���k����
REM EUC�t�@�C���̃R�s�[
COPY %DIR_SMV1370%\SMV1370.sql %PACK_DIR%

echo REM ����������(entity��work�̂�)�� >> %LOGFILE%
echo ����������...
IF NOT %VERSION_ENTITY%==%OLD_VERSION_ENTITY% cscript Marge.vbs %EXCELTOOL_FILEPATH% %OLD_DIR_ENTITY% %DIR_ENTITY% %MARGEFILENAME% %VERSION_PREFIX_ENTITY% %TYPE_ENTITY% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Marge.vbs ���s �G���["
  GOTO ERROR
)
IF NOT %VERSION_ENTITY%==%OLD_VERSION_ENTITY% MKDIR %DIR_MARGE%\%MARGE_DIR_PREFIX_ENTITY% >> %LOGFILE%
IF NOT %VERSION_ENTITY%==%OLD_VERSION_ENTITY% MOVE %EXCELTOOL_FILEDIR%\%MARGEFILENAME% %DIR_MARGE%\%MARGE_DIR_PREFIX_ENTITY% >> %LOGFILE%
IF NOT %VERSION_WORK%==%OLD_VERSION_WORK% cscript Marge.vbs %EXCELTOOL_FILEPATH% %OLD_DIR_WORK% %DIR_WORK% %MARGEFILENAME% %VERSION_PREFIX_WORK% %TYPE_WORK% >> %LOGFILE%
IF ERRORLEVEL 1 (
  SET _ERRMSG="Marge.vbs ���s �G���["
  GOTO ERROR
)
IF NOT %VERSION_WORK%==%OLD_VERSION_WORK% MKDIR %DIR_MARGE%\%MARGE_DIR_PREFIX_WORK% >> %LOGFILE%
IF NOT %VERSION_WORK%==%OLD_VERSION_WORK% MOVE %EXCELTOOL_FILEDIR%\%MARGEFILENAME% %DIR_MARGE%\%MARGE_DIR_PREFIX_WORK% >> %LOGFILE%
echo ������������

REM ���ʃX�N���v�g�̃R�s�[
REM EUC(SMV1370)

REM ��K�h���C�u�֔z�u��
REM --�������쐬--

SET time=%time: =0%
SET /a END_TIME=%time:~0,2% * 60 + %time:~3,2%
SET /a EXECUTE_TIME=%END_TIME% - %START_TIME%

echo %date% %time% >> %LOGFILE%
echo �������Ԃ� %EXECUTE_TIME% ���ł��B >> %LOGFILE%
echo �������Ԃ� %EXECUTE_TIME% ���ł��B

:ERROR
IF ERRORLEVEL 1 (
	ECHO %_ERRMSG%
	pause
)

@echo on
