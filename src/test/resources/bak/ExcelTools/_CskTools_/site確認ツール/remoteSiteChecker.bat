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

REM プロジェクトIDの取得
SET PARAM1=%1
SET MYPATH=%CD%
SET _WORK=%MYPATH:\=,%
for %%f in (%_WORK%) do SET PROJECTID=%%f
if defined PARAM1 SET PROJECTID=%PARAM1%
if "%PARAM1:~0,7%"=="murata-" (
	SET MODULEID=%PARAM1%
) else (
	SET MODULEID=murata-%PROJECTID%
)
	
REM ログファイル名決定
SET LOGFILENAME=mavenRepo-%PROJECTID%-%LOGTIME%.log

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

echo -------------------------------------------------
echo リモートsiteからJUnitとCoverage実行結果取得！
echo -------------------------------------------------
REM テスト結果を確認

pushd %SUMMARY_PATH%
REM echo file:///%URL%/target/site/ > target.txt
echo http://m6f1b012.murata.co.jp/maven/site/murata-app-parent/%MODULEID%/ > target.txt
java -cp bin main.SummaryChecker
more SummaryChecker.log
echo ----------------------------------
echo テスト結果を確認してください
echo ----------------------------------
popd
pause

pushd %COVERAGETRACER_PATH%
REM
echo http://m6f1b012.murata.co.jp/maven/site/murata-app-parent/%MODULEID%/cobertura/index.html > target.txt
java -cp bin main.CoverageTracer
more traceResult\%MODULEID%*.log
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
echo ■Test Summary >> %LOGFILENAME%
type %SUMMARY_PATH%\SummaryChecker.log >>%LOGFILENAME%
echo ■Coverage Check >> %LOGFILENAME%
type %COVERAGETRACER_PATH%\traceResult\%MODULEID%*.log >> %LOGFILENAME%
echo エビデンスをファイルに保存しました：%MYPATH%\%LOGFILENAME%
@echo on