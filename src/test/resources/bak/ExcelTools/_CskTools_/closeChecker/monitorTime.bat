@echo off
rem moniterTime.bat
rem 所要時間計測バッチ
rem Ver 1.0 CSK D.Saruhashi   新規作成
rem Ver 1.1 CSK D.Saruhashi   不具合修正
rem Ver 1.2 CSK D.Saruhashi   時刻が0～9の場合の考慮追加

rem 開始時刻取得
set _TIME=%time%
set _START_TIME=%_TIME%
call :convSec
set _S_TIME=%_TIME_SECOND%

rem 処理実行
call %*

rem 終了時刻取得
set _TIME=%time%
set _END_TIME=%_TIME%
call :convSec 
set _E_TIME=%_TIME_SECOND%

rem 所要時間計算
set /a _PROCESS_TIME=%_E_TIME% - %_S_TIME%
call :formatTime

echo [%_START_TIME:~0,8% - %_END_TIME:~0,8%] PROCESS_TIME = [%_FORMAT_TIME%](%_PROCESS_TIME% sec)

goto :EOF

rem 秒変換
rem 10:20:30 → 36150
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

rem 時刻表示変換
rem 36150 → 10:20:30
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
