@echo off
setlocal enabledelayedexpansion
rem �����̑S�v���_�N�g�ɑ΂���svn update�����{����
rem Author CSK D.Saruhashi
rem Ver 1.0		D.Saruhashi		�V�K�쐬
set _LOGDIR=log\%DATE:/=%
if not exist %_LOGDIR%\nul (mkdir %_LOGDIR%)

echo. > %_LOGDIR%\allUpdate.log
for /F %%D IN ('dir /B /AD') do (
	rem echo %%D
	CD %%D
	
	rem pom.xml�̗L����mvn project������
	if exist pom.xml (
		set _PRODUCT_ID=%%D
		echo [%%D]delete files...
		call :DEL_FILES
		
		echo [%%D]svn update...
		call :SVN_UPDATE
	)
	CD ..
)
goto :EOF

rem �t�@�C���폜
:DEL_FILES
echo ���������������� [!date! !time!][%_PRODUCT_ID%] DELETE Files ����������������      >> ..\%_LOGDIR%\allUpdate.log
del .\src\main\resources\jdbc.properties                                      >> ..\%_LOGDIR%\allUpdate.log 2>&1
del .\src\main\resources\log4j.xml                                            >> ..\%_LOGDIR%\allUpdate.log 2>&1
del .\src\main\resources\springconf\applicationContext-datasource-local.xml   >> ..\%_LOGDIR%\allUpdate.log 2>&1
del .\src\test\resources\springconf\applicationContext-product-test.xml       >> ..\%_LOGDIR%\allUpdate.log 2>&1
del .\src\test\resources\jdbc.properties                                      >> ..\%_LOGDIR%\allUpdate.log 2>&1
del .\src\test\resources\springconf\applicationContext-datasource-local.xml   >> ..\%_LOGDIR%\allUpdate.log 2>&1
goto :EOF

rem svn update
:SVN_UPDATE
echo ���������������� [!date! !time!][%_PRODUCT_ID%]] SVN update ����������������        >> ..\%_LOGDIR%\allUpdate.log
svn update --force >> ..\%_LOGDIR%\allUpdate.log 2>&1
goto :EOF

rem �I������
set _PRODUCT_ID=
set _LOGDIR=
endlocal
