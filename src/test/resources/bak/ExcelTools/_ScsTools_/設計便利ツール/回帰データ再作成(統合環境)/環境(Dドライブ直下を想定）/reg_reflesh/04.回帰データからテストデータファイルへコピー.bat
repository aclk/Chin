ECHO OFF

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

SET TARGET=%*

REM ---�X�L�[�}�A�x�[�X���C���A�o�[�W�����ݒ�B�Ăь�����^�����Ă���΂���𗘗p�B
IF "%BASEDIR%"  =="" SET BASEDIR=D:\evidence
IF "%INPUT%"    =="" SET INPUT=input
IF "%OUTPUT%"   =="" SET OUTPUT=output


SET INPUTDIR=


:COPY_AUTOTESTDATA
ECHO.
ECHO ---------------------------------------
ECHO ��A�f�[�^�t�@�C���̃R�s�[
ECHO ---------------------------------------
ECHO.

ECHO ���̃��[�N�t�H���_����ɂ���
rmdir /S /Q %BASEDIR%
mkdir %BASEDIR%

ECHO �v���_�N�g���Ƃ̃t�H���_���쐬�A�f�[�^���R�s�[
ECHO %TARGET%
SET START_DIR=%~dp0
FOR /D %%D IN (%TARGET%*) DO (
    REM �t�H���_���ړ����܂��邽�߁A���[�v�̍ŏ��Ō��֖߂�
    CD %START_DIR%
    echo %%D\pom.xml
    IF EXIST %%D\pom.xml (
        CD %%D\src\test
        ECHO �t�H���_���쐬
        mkdir %BASEDIR%\%%D\%INPUT%
        mkdir %BASEDIR%\%%D\%OUTPUT%
        ECHO ��A�f�[�^���R�s�[
        FOR /F "usebackq" %%I IN (`DIR /S /B TR_*.xls`) DO (
            xcopy %%I %BASEDIR%\%%D\%INPUT%
        )
        ECHO TR_ �� TP_ �Ƀ��l�[��
        CD %BASEDIR%\%%D\%INPUT%
        RENAME TR_* TP_*
    )
)


:END
ENDLOCAL
