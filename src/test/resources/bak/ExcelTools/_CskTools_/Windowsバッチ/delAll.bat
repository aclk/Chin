@echo off
setlocal enabledelayedexpansion
rem �t�@�C���폜�o�b�`
rem ���ꊇ�폜���Ɂu�v���Z�X�������Ă��ā`�E�E�v�G���[���p�ɂɏo�č폜������Ȃ��ꍇ�Ɋ��p
rem 
rem Ver1.0 CSK D.Saruhashi    �V�K�쐬

rem �����ݒ�
set _EXEC_RESULT=0
rem �G���[�����g���C��
rem set _MAX_DEL_CNT=100

rem �����`�F�b�N
if "%1" == "" (
	set /P _DEL_PATH=�폜�Ώ�PATH����͂��Ă��������F
) else (
	set _DEL_PATH=%1
)


rem �폜���s
:execdel
echo *** all delete start ***
rmdir /Q /S %_DEL_PATH% > nul
dir %_DEL_PATH% > nul
if errorlevel 1 (
	goto end
) else  (
	goto execdel
)
goto end

rem USAGE
:usage

echo usage: delAll [path]
echo.
set _EXEC_RESULT=1

goto end

rem �I��
:end
echo *** all delete done. ***
pause
set _LOGDIR=
exit /b %_EXEC_RESULT%

endlocal
