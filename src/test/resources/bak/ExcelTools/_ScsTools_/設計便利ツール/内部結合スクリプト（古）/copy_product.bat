@ECHO OFF
REM --------------------------
REM �ȉ��͊��ɂ��킹�ĕύX
SET ECLIPSE_HOME=C:\eclipse
SET DEST=scs_online
SET PRODUCTS=C:\ms\meo*
SET TARGET=copy
REM --------------------------
SET ANT_BIN=%ECLIPSE_HOME%\plugins\org.apache.ant_1.6.5\bin
SET ANT_BUILD_FILE=copyproduct.xml

:SELECT_TARGET
SET /P SRC="Input ProductID you want to copy and press enter key.>"

IF "%SRC%"=="all" (GOTO RUN_ANT_ALL)  ^
ELSE (GOTO RUN_ANT)

:RUN_ANT_ALL
FOR /D %%D IN (%PRODUCTS%) DO  ( ^
CALL %ANT_BIN%\ant.bat -f %ANT_BUILD_FILE% %TARGET% -Dsrc.product=%%D -Ddest.product=%DEST%
)
GOTO END

:RUN_ANT
CALL %ANT_BIN%\ant.bat -f %ANT_BUILD_FILE% %TARGET% -Dsrc.product=%SRC% -Ddest.product=%DEST%

:END

REM �V�t���[�����[�N�ł͕s�v�Ȃ̂ŁB
REM :COPY_MESSAGE_RESOURCE
REM CALL %ANT_BIN%\ant.bat -f %ANT_BUILD_FILE% message_resources -Dsrc.product=%%D -Ddest.product=%DEST%

PAUSE
@ECHO ON
