@ECHO OFF

:PREPARE
SETLOCAL ENABLEDELAYEDEXPANSION

CD %~dp0
SET SAITO=%~dp0tools\SAITO

SET TARGET=%*

:TEST
ECHO.
ECHO ---------------------------------------
ECHO SVNÇ©ÇÁç≈êVÇ…çXêV %TARGET%
ECHO ---------------------------------------
ECHO.

IF EXIST %TARGET%\NUL ( ^
	FOR /D %%D IN (%TARGET%*) DO (^
		IF EXIST %%D\pom.xml ( ^
			ECHO %%D revert and update...
			CALL svn revert %%~nD -R
			CALL svn update %%~nD
		)
	)
) ELSE ( ^
	ECHO %TARGET% checkout...
	CALL svn checkout "svn://m6f1b013/prod/spirits/ps/trunk/%TARGET%@HEAD" -r HEAD "%TARGET%" --username "A1B5MEM37" 
)
:END
ENDLOCAL
