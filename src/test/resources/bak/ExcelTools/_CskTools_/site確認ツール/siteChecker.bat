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

SET MYPATH=%CD%

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

echo ----------------------------------
echo JUnit��Coverage���s���ʎ擾�J�n�I
echo ----------------------------------
SET _WORK=%MYPATH:\=,%
for %%f in (%_WORK%) do SET PROJECTID=%%f
REM site���擾
SET SITE_GET=y
IF EXIST %MYPATH%\target\site SET /p SITE_GET="site�����ɑ��݂��邯��site���Ď擾����H(Yes:y / No:n) >"
IF %SITE_GET% == y CALL mvn clean site
REM �e�X�g���ʂ��m�F

pushd %SUMMARY_PATH%
REM file:///C:/eclipse/workspace/sdo0210/target/site/cobertura/index.html
REM         D:\source\goo0750
SET URL=%MYPATH:\=/%
echo file:///%URL%/target/site/ > target.txt
java -cp bin main.SummaryChecker
more SummaryChecker.log
echo ----------------------------------
echo �e�X�g���ʂ��m�F���Ă�������
echo ----------------------------------
popd
pause

pushd %COVERAGETRACER_PATH%
echo file:///%URL%/target/site/cobertura/index.html > target.txt
java -cp bin main.CoverageTracer
more traceResult\%PROJECTID%*.log
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
echo ��Test Summary >> %PROJECTID%-%LOGTIME%.log
type %SUMMARY_PATH%\SummaryChecker.log >> %PROJECTID%-%LOGTIME%.log
echo ��Coverage Check >> %PROJECTID%-%LOGTIME%.log
type %COVERAGETRACER_PATH%\traceResult\%PROJECTID%*.log >> %PROJECTID%-%LOGTIME%.log
echo �G�r�f���X���t�@�C���ɕۑ����܂����F%MYPATH%\%PROJECTID%-%LOGTIME%.log
@echo on