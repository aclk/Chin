@ECHO OFF
REM "�����e�X�g�f�[�^�̃e���v���[�g�𐶐����܂��B�B"
REM �����P �v���_�N�gID   �ȗ�����"0000000"
REM �����Q �e�X�g���{��   �ȗ�����"00"
REM �����R �e�X�g�P�[�XNO �ȗ�����"00"
REM -----
REM �����Atarget\dependency �f�B���N�g���Ƀ��C�u�������Ȃ��ꍇ
REM Maven2 ���C���X�g�[������Ă�����ȉ������s
REM �E�R�}���h�v�����v�g�ŁA���̃f�B���N�g���ipom.xml������j�Ɉړ�
REM �Emvn mvn dependency:copy-dependencies �����s
REM 

SETLOCAL ENABLEDELAYEDEXPANSION

:PREPARE
SET ARG1=0000000
SET ARG2=0.00     
SET ARG3=0-0     


:ARG_CHECK
SET CNT=0
FOR %%I IN (%*) DO SET /A CNT=!CNT!+1
IF %CNT% NEQ 3 GOTO SET_CLASSPATH


:SET_ARGS
SET ARG1=%1
SET ARG2=%2
SET ARG3=%3


:SET_CLASSPATH
CD lib
SET CLASSPATH=%CLASSPATH%
FOR %%I IN (*.jar) DO SET CLASSPATH=!CLASSPATH!;%%~fI


:RUN
CD ..\
SET CLASSPATH=%CLASSPATH%;.;.\springconf;.\template

JAVA murata.co.jointest.EvidenceGeneratorImpl -command template -productId %ARG1% -number %ARG2% -testId %ARG3% -classpath %CLASSPATH%

PAUSE
