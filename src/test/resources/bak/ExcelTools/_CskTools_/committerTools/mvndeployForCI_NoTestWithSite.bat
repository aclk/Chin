@echo off
setlocal enabledelayedexpansion
rem maven deploy/test�o�b�`�p���ݒ�

call mvndeployForCI_NoTest %*
call mvnsite_NoTest %*

endlocal
