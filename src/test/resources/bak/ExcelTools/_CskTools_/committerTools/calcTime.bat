@echo off
setlocal enabledelayedexpansion

rem 時刻計算バッチ

rem SET T=!TIME:/=!
rem echo.
SET T=0%1
SET T=%T:~-8%
SET H=%T:~0,-6%
SET M=%T:~3,-3%
SET S=%T:~6,2%

if %H:~0,1%==0 (set H=%H:~-1%)
if %M:~0,1%==0 (set M=%M:~-1%)
if %S:~0,1%==0 (set S=%S:~-1%)

rem echo T =[%T%] H =[%H%] M =[%M%] S =[%S%]

rem for /l %%i in (1,1,10) do @ping localhost -n 2 > nul

rem SET T1=!TIME:/=!
SET T1=0%2
SET T1=%T1:~-8%
SET H1=%T1:~0,-6%
SET M1=%T1:~3,-3%
SET S1=%T1:~6,2%

if %H1:~0,1%==0 (set H1=%H1:~-1%)
if %M1:~0,1%==0 (set M1=%M1:~-1%)
if %S1:~0,1%==0 (set S1=%S1:~-1%)

rem echo T1=[%T1%] H1=[%H1%] M1=[%M1%] S1=[%S1%]

SET /a H2=H1-H
if %H2% LSS 0 set /a H2=H2+24

SET /a M2=M1-M
if %M2% LSS 0 set /a H2=H2-1
if %M2% LSS 0 set /a M2=M2+60

SET /a S2=S1-S
if %S2% LSS 0 set /a M2=M2-1
if %S2% LSS 0 set /a S2=S2+60


rem SET /a S2=S1-S
rem echo %S2%
rem if %S2% LSS 0 set /a M2=M2-1
rem if %S2% LSS 0 set /a S2=S2+60
rem echo %M2%
rem 
rem SET /a M2=M1-M
rem echo %M1% - %M% = %M2%
rem if %M2% LSS 0 set /a H2=H2-1
rem if %M2% LSS 0 set /a M2=M2+60
rem 
rem SET /a H2=H2+H1-H
rem if %H2% LSS 0 set /a H2=H2+24

SET H2=0%H2%
SET M2=0%M2%
SET S2=0%S2%

echo [%H2:~-2%:%M2:~-2%:%S2:~-2%] from %T% to %T1%
rem echo [%H2:~-2%:%M2:~-2%:%S2:~-2%]

endlocal
