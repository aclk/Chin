@echo off
REM 環境設定
SET ZIP_PATH="C:\Program Files\7-Zip\7z"
SET SUMMARY_PATH=D:\work\SVN\CSK\sandbox\public\tools\summaryChecker\SummaryChecker
SET COVERAGETRACER_PATH=D:\work\SVN\CSK\sandbox\public\tools\coverageTracer\coverageTracer
SET SITE_COPY_KDRIVE_PATH=K:\UPトレーニング\国内生販システム再構築\個人フォルダ\A1B5MEMH\CSK管理フォルダ\70開発\30.プロジェクトsite

REM 実行時間の取得
SET LOGTIME=%date%_%time:~0,5%
SET LOGTIME=%LOGTIME:/=%
SET LOGTIME=%LOGTIME::=%
SET LOGTIME=%LOGTIME: =0%

SET MYPATH=%CD%

echo ----------------------------------
echo compile...
echo ----------------------------------
pushd %SUMMARY_PATH%\src
javac -d ../bin main/*.java
popd

pushd %COVERAGETRACER_PATH%\src
javac -d ../bin main/*.java
popd

echo compile done.

echo ----------------------------------
echo JUnitとCoverage実行結果取得開始！
echo ----------------------------------
SET _WORK=%MYPATH:\=,%
for %%f in (%_WORK%) do SET PROJECTID=%%f
REM siteを取得
SET SITE_GET=y
IF EXIST %MYPATH%\target\site SET /p SITE_GET="siteが既に存在するけどsiteを再取得する？(Yes:y / No:n) >"
IF %SITE_GET% == y CALL mvn clean site
REM テスト結果を確認

pushd %SUMMARY_PATH%
REM file:///C:/eclipse/workspace/sdo0210/target/site/cobertura/index.html
REM         D:\source\goo0750
SET URL=%MYPATH:\=/%
echo file:///%URL%/target/site/ > target.txt
java -cp bin main.SummaryChecker
more SummaryChecker.log
echo ----------------------------------
echo テスト結果を確認してください
echo ----------------------------------
popd
pause

pushd %COVERAGETRACER_PATH%
echo file:///%URL%/target/site/cobertura/index.html > target.txt
java -cp bin main.CoverageTracer
more traceResult\%PROJECTID%*.log
echo ----------------------------------
echo Coverage結果を確認してください
echo ----------------------------------
popd
pause
REM siteをzip化
REM %ZIP_PATH% a -tzip %MYPATH%\target\site(%PROJECTID%).zip %MYPATH%\target\site
REM Kドライブにコピー
REM IF NOT EXIST %SITE_COPY_KDRIVE_PATH%\backup MKDIR %SITE_COPY_KDRIVE_PATH%\backup
REM IF EXIST %SITE_COPY_KDRIVE_PATH%\site(%PROJECTID%).zip MOVE /Y %SITE_COPY_KDRIVE_PATH%\site(%PROJECTID%).zip %SITE_COPY_KDRIVE_PATH%\backup\site(%PROJECTID%-%LOGTIME%).zip
REM COPY %MYPATH%\target\site(%PROJECTID%).zip %SITE_COPY_KDRIVE_PATH%
REM echo Kドライブに保存完了:%SITE_COPY_KDRIVE_PATH%site(%PROJECTID%).zip
REM pause

echo ----------------------------------
echo エビデンスを保存する？
echo ----------------------------------
pause
echo ■Test Summary >> %PROJECTID%-%LOGTIME%.log
type %SUMMARY_PATH%\SummaryChecker.log >> %PROJECTID%-%LOGTIME%.log
echo ■Coverage Check >> %PROJECTID%-%LOGTIME%.log
type %COVERAGETRACER_PATH%\traceResult\%PROJECTID%*.log >> %PROJECTID%-%LOGTIME%.log
echo エビデンスをファイルに保存しました：%MYPATH%\%PROJECTID%-%LOGTIME%.log
@echo on