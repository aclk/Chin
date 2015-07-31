set MINGW=C:\MinGW
set PATH_O=%PATH%
set PATH=%MINGW%\bin
windres Icon1.rc Icon1.o
gcc -std=c99 -c jRegExAnalyser.c
gcc -mwindows -o jRegExAnalyser.exe jRegExAnalyser.o Icon1.o
set PATH=%PATH_O%
