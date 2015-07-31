@ECHO OFF

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

SET TARGET=%*

REM ---�X�L�[�}�A�x�[�X���C���A�o�[�W�����ݒ�B�Ăь�����^�����Ă���΂���𗘗p�B
IF "%SCHEMA%"  =="" SET SCHEMA=SS
IF "%BASELINE%"=="" SET BASELINE=MURATA-PS-BASELINE
IF "%VERSION%" =="" SET VERSION=(,0.5.52-sys-999)


:REWRITE_SETTINGS
ECHO.
ECHO ---------------------------------------
ECHO �V�e�X�gFW�ݒ�t�@�C���̃`�F�b�N������
ECHO -root             %TARGET%
ECHO -schema           %SCHEMA%
ECHO -baseline         %BASELINE%
ECHO -version          %VERSION%
ECHO ---------------------------------------
ECHO.
FOR /D %%D IN (%TARGET%*) DO ^
java -jar tools/rewriter-0.0.12.jar -root %%D -schema %SCHEMA% -baseline %BASELINE% -version %VERSION% -autotest
)


:COPY_SETTINGS
ECHO.
ECHO ---------------------------------------
ECHO �e�X�g�ڑ��ݒ�t�@�C���̃R�s�[
ECHO ---------------------------------------
ECHO.

FOR /D %%D IN (%TARGET%*) DO ^
IF EXIST %%D\pom.xml ( ^
CD %%D
ECHO.----- %%D -----
ECHO.--- CASE OF UC
XCOPY "%SAITO%\jdbc.properties" "src\main\resources\" /F /U /Y
XCOPY "%SAITO%\applicationContext-datasource-local.xml" "src\main\resources\springconf\" /F /U /Y
XCOPY "%SAITO%\applicationContext-datasource-pooling-local.xml" "src\main\resources\springconf\" /F /U /Y
XCOPY "%SAITO%\log4j.xml" "src\main\resources\" /F /U /Y
ECHO.--- CASE OF BSP
XCOPY "%SAITO%\jdbc.properties" "src\test\resources\" /F /U /Y
XCOPY "%SAITO%\applicationContext-datasource-local.xml" "src\test\resources\springconf\" /F /U /Y
XCOPY "%SAITO%\log4j.xml" "src\main\resources\" /F /U /Y
CD %~dp0
)


:END
ENDLOCAL
