@echo off
setlocal
rem スキーマ全テーブルtruncateバッチ

if "%1" == "" goto USAGE
if "%2" == "" goto USAGE

echo all table truncate ...
sqlplus -s %1/%1@%2 @%~dp0sql\truncate_all_tables.sql
echo truncate done.

goto :END

:USAGE

echo USAGE:
echo   truncateSchema.bat [user] [SID]
echo.

:END
endlocal
