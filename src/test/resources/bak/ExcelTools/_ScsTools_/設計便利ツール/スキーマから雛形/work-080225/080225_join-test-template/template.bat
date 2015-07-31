@ECHO OFF
REM "結合テストデータのテンプレートを生成します。。"
REM 引数１ プロダクトID   省略時は"0000000"
REM 引数２ テスト実施数   省略時は"00"
REM 引数３ テストケースNO 省略時は"00"
REM -----
REM もし、target\dependency ディレクトリにライブラリがない場合
REM Maven2 がインストールされている環境以下を実行
REM ・コマンドプロンプトで、このディレクトリ（pom.xmlがある）に移動
REM ・mvn mvn dependency:copy-dependencies を実行
REM 

SETLOCAL ENABLEDELAYEDEXPANSION

:PREPARE
SET ARG1=0000000
SET ARG2=0.00     
SET ARG3=0-0     


:ARG_CHECK
SET CNT=0
FOR %%I IN (%*) DO SET /A CNT=!CNT!+1
IF %CNT% NEQ 3 GOTO SET_CLASSPATH


:SET_ARGS
SET ARG1=%1
SET ARG2=%2
SET ARG3=%3


:SET_CLASSPATH
CD lib
SET CLASSPATH=%CLASSPATH%
FOR %%I IN (*.jar) DO SET CLASSPATH=!CLASSPATH!;%%~fI


:RUN
CD ..\
SET CLASSPATH=%CLASSPATH%;.;.\springconf;.\template

JAVA murata.co.jointest.EvidenceGeneratorImpl -command template -productId %ARG1% -number %ARG2% -testId %ARG3% -classpath %CLASSPATH%

PAUSE
