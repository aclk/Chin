@ECHO OFF
REM ---------------------------------------
REM �G�r�f���X�o�͂̂��߂̃e�X�g���s
REM ---------------------------------------

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

REM ---�^�[�Q�b�g�ݒ�B����������΂����̃v���_�N�g�B�Ȃ���΃t�H���_�����ׂ�
SET TARGET=%*
IF "%*"=="" SET TARGET=%~dp0

REM ���O�̃f�B���N�g�����쐬����B
IF NOT EXIST logs\autotest_logs MKDIR logs\autotest_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO �e�X�g���{
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		CD %%~nD
		SET PROD_ID=%%~nD
		mvn clean test > %~dp0\logs\autotest_logs\test_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~1,1%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		CD %~dp0) 
)
ENDLOCAL
