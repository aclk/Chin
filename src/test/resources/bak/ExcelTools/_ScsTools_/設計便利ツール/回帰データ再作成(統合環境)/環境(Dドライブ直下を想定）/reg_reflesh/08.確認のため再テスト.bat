@ECHO OFF
REM ---------------------------------------
REM �m�F�̂��߂̍ăe�X�g���s
REM ---------------------------------------

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

REM ---�^�[�Q�b�g�ݒ�B����������΂����̃v���_�N�g�B�Ȃ���΃t�H���_�����ׂ�
SET TARGET=%*
IF "%*"=="" SET TARGET=%~dp0

REM ���O�̃f�B���N�g�����쐬����B
IF NOT EXIST logs\confirmtest_logs MKDIR logs\confirmtest_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO �m�F�̂��߂̍ăe�X�g
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		CD %%~nD
		SET PROD_ID=%%~nD
		mvn clean site > %~dp0\logs\confirmtest_logs\test_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~1,1%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		CD %~dp0) 
)
ENDLOCAL
