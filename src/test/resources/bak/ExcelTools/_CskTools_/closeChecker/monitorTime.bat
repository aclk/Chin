@echo off
rem moniterTime.bat
rem ���v���Ԍv���o�b�`
rem Ver 1.0 CSK D.Saruhashi   �V�K�쐬
rem Ver 1.1 CSK D.Saruhashi   �s��C��
rem Ver 1.2 CSK D.Saruhashi   ������0�`9�̏ꍇ�̍l���ǉ�

rem �J�n�����擾
set _TIME=%time%
set _START_TIME=%_TIME%
call :convSec
set _S_TIME=%_TIME_SECOND%

rem �������s
call %*

rem �I�������擾
set _TIME=%time%
set _END_TIME=%_TIME%
call :convSec 
set _E_TIME=%_TIME_SECOND%

rem ���v���Ԍv�Z
set /a _PROCESS_TIME=%_E_TIME% - %_S_TIME%
call :formatTime

echo [%_START_TIME:~0,8% - %_END_TIME:~0,8%] PROCESS_TIME = [%_FORMAT_TIME%](%_PROCESS_TIME% sec)

goto :EOF

rem �b�ϊ�
rem 10:20:30 �� 36150
:convSec
rem echo --:convSec--
if "%_TIME:~0,1%" == " " (set _TMP_TIME=0%_TIME:~1,7%) else set _TMP_TIME=%_TIME%
if %_TMP_TIME:~0,1% == 0 (set _HH=%_TMP_TIME:~1,1%) else set _HH=%_TMP_TIME:~0,2%
set /a _HH=%_HH% * 60 * 60
if %_TMP_TIME:~3,1% == 0 (set _MM=%_TMP_TIME:~4,1%) else set _MM=%_TMP_TIME:~3,2%
set /a _MM=%_MM% * 60
if %_TMP_TIME:~6,1% == 0 (set _SS=%_TMP_TIME:~7,1%) else set _SS=%_TMP_TIME:~6,2%

set /a _TIME_SECOND=%_HH% + %_MM% + %_SS%

goto :EOF

rem �����\���ϊ�
rem 36150 �� 10:20:30
:formatTime
rem echo --:formatTime--
set /a _HH=%_PROCESS_TIME% / 60 / 60
set /a _MM=(%_PROCESS_TIME% - (%_HH% * 60 * 60)) / 60
set /a _SS=%_PROCESS_TIME% - (%_HH% * 60 * 60) - (%_MM% * 60)
set _HH=0%_HH%
set _MM=0%_MM%
set _SS=0%_SS%
set _FORMAT_TIME=%_HH:~-2%:%_MM:~-2%:%_SS:~-2%
goto :EOF

:EOF
set _TMP_TIME=
set _TIME=
set _S_TIME=
set _E_TIME=
set _START_TIME=
set _END_TIME=
set _PROCESS_TIME=
set _TIME_SECOND=
@echo on
