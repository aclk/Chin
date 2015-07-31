@echo off
setlocal enabledelayedexpansion
rem maven deploy/testバッチ用環境設定

call mvndeploy_NoTest %*
call mvnsite_NoTest %*

endlocal
