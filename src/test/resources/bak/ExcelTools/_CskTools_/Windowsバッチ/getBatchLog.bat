@echo off
setlocal enabledelayedexpansion
rem �o�b�`���O�擾�p�o�b�`
rem Ver1.0 CSK D.Saruhashi

rem ��������
rem #### �p�X���[�h�ύX���A�z�X�g�ύX���͗v�����e #####
set _USER=rscsk
set _PASS=xxxx
set _BATCH_HOST=m0tabt
rem #####################################
set _FTP_CMD_FILE=_ftp_cmd.txt
set _EXEC_RESULT=0

rem �����`�F�b�N
if "%1" == "" goto usage
if "%2" == "" goto usage
if "%3" == "" goto usage

rem ���O�擾
:execGetLog

echo open %_BATCH_HOST%> %_FTP_CMD_FILE%
echo %_USER%>> %_FTP_CMD_FILE%
echo %_PASS%>> %_FTP_CMD_FILE%
echo cd /murata/spirits%1/log/%2>> %_FTP_CMD_FILE%
echo get %3-std.log>> %_FTP_CMD_FILE%
echo get %3-err.log>> %_FTP_CMD_FILE%
echo quit>> %_FTP_CMD_FILE%

ftp -s:_ftp_cmd.txt

del %_FTP_CMD_FILE%

goto :end

rem USAGE
:usage

echo usage: getBatchLog [home dir spirits number] [subsystem name] [JOB name]
echo.
echo ex) getBatchLog 100 ss JSJD734
echo.
set _EXEC_RESULT=1

rem �I��
:end

exit /b %_EXEC_RESULT%
endlocal