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
IF NOT EXIST logs\rdcreate_logs MKDIR logs\rdcreate_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO ��A�f�[�^�쐬�B
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		CD %%~nD
		ECHO %%D��Test���W���[�����݂ŃR���p�C���BTarget���ɐݒ�t�@�C�������o���A��������ɉ�A�f�[�^�쐬�B
		mvn clean test-compile rdconv:convert > %~dp0\logs\rdcreate_logs\rdcreate_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~1,1%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		REM ��K�w�オ��
		CD %~dp0
		) 
)
ENDLOCAL
