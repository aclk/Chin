@echo off
setlocal enabledelayedexpansion
rem svn check tagバッチ

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

rem add tag実行
:exectest

rem if "%1" == "mssys" (
rem 	set _SVN_DIR=%_SVN_REMOTE_URL%/%_SVN_MSSYS_BRANCH%
rem 	set _BRANCH_NAME=[mssys]
rem 	set _PRODUCT_ID=%2
rem 	set _PRODUCT_VER=%3
rem ) else (
rem 	set _SVN_DIR=%_SVN_REMOTE_URL%/%_SVN_TRUNK%
rem 	set _PRODUCT_ID=%1
rem 	set _PRODUCT_VER=%2
rem )

svn cat --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER%/%_PRODUCT_ID%/pom.xml | more

goto end

rem USAGE
:usage

echo usage: svnCheckTag {option} [productId] [version]
echo.
echo    options:
echo       sstunk , dbmtrunk , mstrunk , pstrunk , bmctrunk , sszn , dbmzn , mszn , pszn
echo.

goto end

rem 終了
:end

endlocal
