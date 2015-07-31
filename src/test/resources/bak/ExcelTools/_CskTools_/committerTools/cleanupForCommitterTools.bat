@echo off
setlocal
rem maven committerTools用クリーンアップバッチ
rem Ver1.0 CSK D.Saruhashi

if exist src\test\resources\log4j.xml.bak (
	copy src\test\resources\log4j.xml.bak src\test\resources\log4j.xml > nul
	del src\test\resources\log4j.xml.bak
)
if exist src\main\resources\log4j.xml.bak (
	copy src\main\resources\log4j.xml.bak src\main\resources\log4j.xml > nul
	del src\main\resources\log4j.xml.bak
)

endlocal
