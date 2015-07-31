@echo off
setlocal enabledelayedexpansion
rem maven deploy/testバッチ用環境設定

rem process_type test:mvn test deploy:mvn deploy deploy_ci:rel all deploy_notest deploy_ci_notest
set _PROCESS_TYPE=deploy_ci_notest
call setSchemaEnv

call mvntool_core %*

exit /b %_EXEC_RESULT%
endlocal
