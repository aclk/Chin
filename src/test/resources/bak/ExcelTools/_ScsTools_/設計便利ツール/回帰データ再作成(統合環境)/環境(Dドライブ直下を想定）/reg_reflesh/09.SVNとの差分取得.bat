@ECHO OFF
REM ---------------------------------------
REM �f�[�^����������������SVN�Ƃ̍�������胍�O���c���B
REM ---------------------------------------

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

REM ---�^�[�Q�b�g�ݒ�B����������΂����̃v���_�N�g�B�Ȃ���΃t�H���_�����ׂ�
SET TARGET=%*
IF "%*"=="" SET TARGET=%~dp0

REM ���O�̃f�B���N�g�����쐬����B
IF NOT EXIST logs\svndiff_logs MKDIR logs\svndiff_logs



:TEST
ECHO.
ECHO ---------------------------------------
ECHO SVN��DIFF
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ( ^
	IF EXIST %%D\pom.xml ( ^
		svn st %%D > %~dp0\logs\svndiff_logs\diff_%%~nD_%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%_%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%%TIME:~9,2%.log 2>&1
		) 
)
ENDLOCAL
