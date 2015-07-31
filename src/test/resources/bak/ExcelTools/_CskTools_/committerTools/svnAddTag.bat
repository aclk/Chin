@echo off
setlocal enabledelayedexpansion
rem svn add tagバッチ

rem 初期処理
set _MSSYS=false
set _LOGDIR=log\%DATE:/=%
rem set _SVN_REMOTE_URL=svn://m6f1b013/prod
rem set _SVN_REMOTE_URL=http://hq1b216/svn/dev
rem set _SVN_TRUNK=trunk
rem set _SVN_TAGS=tags
rem set _SVN_MSSYS_BRANCH=branches/bmc-systemtest-sysm
call setSVNEnv %*

if errorlevel 1 goto :usage

if not exist %_LOGDIR%\nul (mkdir %_LOGDIR%)

rem 引数チェック
if "%1" == "" goto usage
if "%2" == "" goto usage

rem svn verチェック
set _VERCHK=
for /f "delims=" %%F in ('svn --version ^| find "バージョン 1.4"'
) do set _VERCHK=!_VERCHK! %%F

rem add tag実行
:exectest

rem if "%1" == "mssys" (
rem 	rem set _SVN_DIR=%_SVN_REMOTE_URL%/%_SVN_MSSYS_BRANCH%
rem 	rem set _BRANCH_NAME=[mssys]
rem 	set _PRODUCT_ID=%2
rem 	set _PRODUCT_VER=%3
rem ) else (
rem 	rem set _SVN_DIR=%_SVN_REMOTE_URL%/%_SVN_TRUNK%
rem 	set _PRODUCT_ID=%1
rem 	set _PRODUCT_VER=%2
rem )

echo ☆★☆★☆★☆★ [%date% %time%][%_PRODUCT_ID%] svn add tag start ☆★☆★☆★☆★ >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log

echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn pom取得中...
rem call ant -f %~dp0\build-svn.xml -Dbase_dir=%CD% -Dproject_name=%1 -Dmssys=true get_pom >> %_LOGDIR%\checkout_%1.log
svn cat --username %_SVN_USER% --password %_SVN_USER% %_SVN_SOURCE%/%_PRODUCT_ID%/pom.xml | more
echo ★★pom.xmlに記載されたバージョンが[%_PRODUCT_VER%]と等しいか確認して、誤りであればCTRL-Cで本バッチを停止して下さい!!★★
pause

echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag start...

svn delete --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "Tag削除 "%_PRODUCT_VER% >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
if "!_VERCHK!"=="" (
	svn mkdir --parents --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "リリース "%_PRODUCT_VER%                         >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
) else (
	svn mkdir --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID% -m "リリース "%_PRODUCT_VER%                         >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
	svn mkdir --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "リリース "%_PRODUCT_VER%          >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
)
svn copy --username %_SVN_USER% --password %_SVN_USER% %_SVN_SOURCE%/%_PRODUCT_ID% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "リリース "%_PRODUCT_VER% >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1

if errorlevel 1 (
	echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag Failure 
) else (
	echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag Success 
	echo ★★commitしたtagより取得して正常にtag付けできたか確認します★★
	call svnCheckTag %_BRANCH_NAME% %_PRODUCT_ID% %_PRODUCT_VER%
)
echo ☆★☆★☆★☆★ [%date% %time%][%_PRODUCT_ID%] svn add tag end   ☆★☆★☆★☆★ >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log

echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag end. 

goto end

rem USAGE
:usage

echo usage: svnAddTag {option} [productId] [version]
echo.
echo    options:
echo       sstrunk , dbmtrunk , mstrunk , pstrunk , bmctrunk , sszn , dbmzn , mszn , pszn
echo.

goto end

rem 終了
:end

endlocal
