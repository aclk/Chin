@echo off
setlocal enabledelayedexpansion
rem maven deploy/test�o�b�`�p���ݒ�

call mvndeploy_NoTest %*
call mvnsite_NoTest %*

endlocal
