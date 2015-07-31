@echo off
REM ���ݒ�
SET ZIP_PATH="C:\Program Files\7-Zip\7z"
SET SUMMARY_PATH=D:\work\SVN\CSK\sandbox\public\tools\summaryChecker\SummaryChecker
SET COVERAGETRACER_PATH=D:\work\SVN\CSK\sandbox\public\tools\coverageTracer\coverageTracer
SET SITE_COPY_KDRIVE_PATH=K:\UP�g���[�j���O\�������̃V�X�e���č\�z\�l�t�H���_\A1B5MEMH\CSK�Ǘ��t�H���_\70�J��\30.�v���W�F�N�gsite

REM ���s���Ԃ̎擾
SET LOGTIME=%date%_%time:~0,5%
SET LOGTIME=%LOGTIME:/=%
SET LOGTIME=%LOGTIME::=%
SET LOGTIME=%LOGTIME: =0%

REM �v���W�F�N�gID�̎擾
SET PARAM1=%1
SET MYPATH=%CD%
SET _WORK=%MYPATH:\=,%
for %%f in (%_WORK%) do SET PROJECTID=%%f
if defined PARAM1 SET PROJECTID=%PARAM1%
if "%PARAM1:~0,7%"=="murata-" (
	SET MODULEID=%PARAM1%
) else (
	SET MODULEID=murata-%PROJECTID%
)
	
REM ���O�t�@�C��������
SET LOGFILENAME=mavenRepo-%PROJECTID%-%LOGTIME%.log

echo ----------------------------------
echo compile...
echo ----------------------------------
pushd %SUMMARY_PATH%\src
javac -d ../bin main/*.java
popd

pushd %COVERAGETRACER_PATH%\src
javac -d ../bin main/*.java
popd

echo compile done.

echo -------------------------------------------------
echo �����[�gsite����JUnit��Coverage���s���ʎ擾�I
echo -------------------------------------------------
REM �e�X�g���ʂ��m�F

pushd %SUMMARY_PATH%
REM echo file:///%URL%/target/site/ > target.txt
echo http://m6f1b012.murata.co.jp/maven/site/murata-app-parent/%MODULEID%/ > target.txt
java -cp bin main.SummaryChecker
more SummaryChecker.log
echo ----------------------------------
echo �e�X�g���ʂ��m�F���Ă�������
echo ----------------------------------
popd
pause

pushd %COVERAGETRACER_PATH%
REM
echo http://m6f1b012.murata.co.jp/maven/site/murata-app-parent/%MODULEID%/cobertura/index.html > target.txt
java -cp bin main.CoverageTracer
more traceResult\%MODULEID%*.log
echo ----------------------------------
echo Coverage���ʂ��m�F���Ă�������
echo ----------------------------------
popd
pause
REM site��zip��
REM %ZIP_PATH% a -tzip %MYPATH%\target\site(%PROJECTID%).zip %MYPATH%\target\site
REM K�h���C�u�ɃR�s�[
REM IF NOT EXIST %SITE_COPY_KDRIVE_PATH%\backup MKDIR %SITE_COPY_KDRIVE_PATH%\backup
REM IF EXIST %SITE_COPY_KDRIVE_PATH%\site(%PROJECTID%).zip MOVE /Y %SITE_COPY_KDRIVE_PATH%\site(%PROJECTID%).zip %SITE_COPY_KDRIVE_PATH%\backup\site(%PROJECTID%-%LOGTIME%).zip
REM COPY %MYPATH%\target\site(%PROJECTID%).zip %SITE_COPY_KDRIVE_PATH%
REM echo K�h���C�u�ɕۑ�����:%SITE_COPY_KDRIVE_PATH%site(%PROJECTID%).zip
REM pause

echo ----------------------------------
echo �G�r�f���X��ۑ�����H
echo ----------------------------------
pause
echo ��Test Summary >> %LOGFILENAME%
type %SUMMARY_PATH%\SummaryChecker.log >>%LOGFILENAME%
echo ��Coverage Check >> %LOGFILENAME%
type %COVERAGETRACER_PATH%\traceResult\%MODULEID%*.log >> %LOGFILENAME%
echo �G�r�f���X���t�@�C���ɕۑ����܂����F%MYPATH%\%LOGFILENAME%
@echo on