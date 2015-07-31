@echo off
setlocal enabledelayedexpansion
rem maven close�Y��`�F�b�N�o�b�`
rem Ver1.0 CSK D.Saruhashi

rem ��������
set _DB_SETTING_JAR_PATH=%~dp0\DB�ݒ菑������
set _LOGDIR=log\%DATE:/=%
set _DB_SCHEMA=sys1
set _PROCESS_TYPE=closeCheck

rem set _PROCESS_TYPE=test
rem set _RESULT_FILE=%_LOGDIR%\%DATE:/=%_test_%_DB_SCHEMA%.log
set _EXEC_RESULT=0
set _T1=!TIME:~0,8!
set _T2=!TIME:~0,8!
if not exist %_LOGDIR%\nul (mkdir %_LOGDIR%)

rem �����`�F�b�N
if "%1" == "" goto usage

rem ��A�e�X�g���s
:exectest
if "%1" == "" goto end
if exist %1\nul (

set _T1=!TIME:~0,8!

rem �X�L�[�}�ύX
echo [!time:~0,8!][%1] dataSouce [%_DB_SCHEMA%] Change. 
java -jar %_DB_SETTING_JAR_PATH%\DBSetting.jar %1 0 %_DB_SCHEMA%

cd %1

rem close check ����
echo [!time:~0,8!][%1] close check ������...
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

echo [!time:~0,8!][%1] close check ���{��...
echo ���������������� [!date! !time!][%1] close check start ���������������� >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
call monitorTime.bat mvn clean test >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
copy CloseCheck.log %1-CloseCheck.log > nul
del CloseCheck.log
find /C "�^�킵��" %1-CloseCheck.log
call cleanupForCloseCheck
echo ���������������� [!date! !time!][%1] close check end   ���������������� >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
echo [!time:~0,8!][%1] close check done...
cd ..
) else echo [!time:~0,8!] "%1" �v���W�F�N�g�����݂��܂���.
shift /1
goto exectest

rem USAGE
:usage

echo usage: closeCheck [productId] [productId] ...
echo.
set _EXEC_RESULT=1

goto end

rem �I��
:end
set /P x=total elapsed�F<NUL
call calcTime %_T2% !TIME:~0,8!
exit /b %_EXEC_RESULT%
endlocal
