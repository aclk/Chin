@echo off
setlocal enabledelayedexpansion
rem maven deploy/test�o�b�`
rem Ver1.0 CSK D.Saruhashi
rem Ver1.1 CSK D.Saruhashi    �����\�����X�V����Ă��Ȃ����������i�x�����ϐ��W�J�j
rem Ver1.2 CSK D.Saruhashi    ���������ԕ\�L�ǉ��Asite_nodeploy�ǉ�

rem ��������
set _DB_SETTING_JAR_PATH=%~dp0\DB�ݒ菑������
set _LOGDIR=log\%DATE:/=%
rem set _DB_SCHEMA=ms1
rem process_type test:mvn test deploy:mvn deploy deploy_ci:rel all
rem set _PROCESS_TYPE=test
set _RESULT_FILE=%_LOGDIR%\%DATE:/=%_test_%_DB_SCHEMA%.log
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
echo [!time:~0,8!][%1] dataSouce [%_DB_SCHEMA%] Change. 

java -jar %_DB_SETTING_JAR_PATH%\DBSetting.jar %1 0 %_DB_SCHEMA%


echo [!time:~0,8!][%1] maven %_PROCESS_TYPE%���{��...

cd %1
if exist src\test\resources\log4j.xml (
	if "%_PROCESS_TYPE%"=="site_forcov" (
		copy src\test\resources\log4j.xml src\test\resources\log4j.xml.bak > nul
		copy %~dp0\simple_log4j_forCov.xml src\test\resources\log4j.xml > nul
	) else (
		copy src\test\resources\log4j.xml src\test\resources\log4j.xml.bak > nul
		copy %~dp0\simple_log4j.xml src\test\resources\log4j.xml > nul
	)
)
if exist src\main\resources\log4j.xml (
	if "%_PROCESS_TYPE%"=="site_forcov" (
		copy src\main\resources\log4j.xml src\main\resources\log4j.xml.bak > nul
		copy %~dp0\simple_log4j_forCov.xml src\main\resources\log4j.xml > nul
	) else (
		copy src\main\resources\log4j.xml src\main\resources\log4j.xml.bak > nul
		copy %~dp0\simple_log4j.xml src\main\resources\log4j.xml > nul
	)
)
echo ���������������� [!date! !time!][%1] maven %_PROCESS_TYPE% start ���������������� >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
if "%_PROCESS_TYPE%"=="test" (
call refreshMessage up %_DB_SCHEMA% xe
	call monitorTime.bat mvn clean test >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="deploy" (
	call monitorTime.bat rel >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="deploy_ci" (
	call monitorTime.bat rel all >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="deploy_notest" (
	call monitorTime.bat  mvn -Ddeploy.mode=deploy -Ddeploy.packaging=jar -Dmaven.test.skip=true clean source:jar javadoc:jar deploy >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="deploy_ci_notest" (
	call monitorTime.bat mvn -Ddeploy.mode=deploy -Ddeploy.packaging=war -Dmaven.test.skip=true clean source:jar javadoc:jar deploy >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
	call monitorTime.bat mvn -Ddeploy.mode=deploy -Ddeploy.packaging=jar -Dmaven.test.skip=true clean deploy >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="site" (
	call monitorTime.bat mvn -Ddeploy.mode=deploy  -Ddeploy.packaging=jar clean site site:deploy >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="site_notest" (
	call monitorTime.bat mvn -Ddeploy.mode=deploy  -Ddeploy.packaging=jar -Dmaven.test.skip=true clean site site:deploy >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="site_nodeploy" (
	call monitorTime.bat mvn -Ddeploy.mode=deploy  -Ddeploy.packaging=jar clean site >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
) else if "%_PROCESS_TYPE%"=="site_forcov" (
	call monitorTime.bat mvn -Ddeploy.mode=deploy  -Ddeploy.packaging=jar clean site >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log 2>&1
)
if errorlevel 1 (
	set _EXEC_RESULT=1
	echo ���������������� [!date! !time!][%1] maven %_PROCESS_TYPE% Failure   ���������������� >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
	set /P x=[!time:~0,8!][%1] maven %_PROCESS_TYPE% Failure  elapsed�F<NUL
	call calcTime !_T1! !TIME:~0,8!
	echo %1	�~ >> ..\%_RESULT_FILE%
) else (
	echo ���������������� [!date! !time!][%1] maven %_PROCESS_TYPE% Success   ���������������� >> ..\%_LOGDIR%\%_PROCESS_TYPE%_%1.log
	set /P x=[!time:~0,8!][%1] maven %_PROCESS_TYPE% Success  elapsed�F<NUL
	call calcTime !_T1! !TIME:~0,8!
	echo %1	�� >> ..\%_RESULT_FILE%
)
call cleanupForCommitterTools.bat
cd ..
) else echo [!time:~0,8!] "%1" �v���W�F�N�g�����݂��܂���.
shift /1
goto exectest

rem USAGE
:usage

echo usage: mvn%_PROCESS_TYPE% [productId] [productId] ...
echo.
set _EXEC_RESULT=1

goto end

rem �I��
:end
set /P x=total elapsed�F<NUL
call calcTime %_T2% !TIME:~0,8!
exit /b %_EXEC_RESULT%
endlocal
