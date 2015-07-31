@echo off
setlocal
rem svn checkoutバッチ

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

rem checkout実行
:exectest
if "%1" == "" goto end

echo ☆★☆★☆★☆★ [%date% %time%][%1] svn checkout start ☆★☆★☆★☆★ >> %_LOGDIR%\checkout_%1.log
if not "%_BRANCH_NAME%" == "" (
	if "%_BRANCH_FLAG%" == "" (
		set _BRANCH_FLAG=true
		shift /1
		goto exectest
	)
)

if exist %1 (
	echo [%time%][%1] プロジェクトdir削除中...
	rmdir /S /Q %1
)

rem if "%_MSSYS%" == "true" (
rem 	echo [%time%][mssys][%1] svn checkout実施中...
rem 	svn co --username %_SVN_USER% --password %_SVN_USER% %_SVN_REMOTE_URL%/%_SVN_MSSYS_BRANCH%/%1 %1 >> %_LOGDIR%\checkout_%1.log
rem 	if errorlevel 1 (
rem 		echo [%time%][mssys][%1] svn checkout再実施中...
rem 		call ant -f %~dp0\build-svn.xml -Dbase_dir=%CD% -Dproject_name=%1 -Dmssys=true checkout >> %_LOGDIR%\checkout_%1.log
rem 
rem 	)
rem ) else (
rem 	echo [%time%][%1] svn checkout実施中...
rem 	svn co --username %_SVN_USER% --password %_SVN_USER% %_SVN_REMOTE_URL%/%_SVN_TRUNK%/%1 %1 >> %_LOGDIR%\checkout_%1.log
rem 	if errorlevel 1 (
rem 		echo [%time%][%1] svn checkout再実施中...
rem 		call ant -f %~dp0\build-svn.xml -Dbase_dir=%CD% -Dproject_name=%1 checkout >> %_LOGDIR%\checkout_%1.log
rem 	)
rem )
echo %_SVN_SOURCE%/%1
echo [%time%][%_BRANCH_NAME%][%1] svn checkout実施中...
svn co --username %_SVN_USER% --password %_SVN_USER% %_SVN_SOURCE%/%1 %1 >> %_LOGDIR%\checkout_%1.log
if errorlevel 1 (
	echo [%time%][%_BRANCH_NAME%][%1] svn checkout再実施中...
	call ant -f %~dp0\build-svn.xml -Dbase_dir=%CD% -Dsvn_path=%_SVN_SOURCE% -Dproject_name=%1 checkout >> %_LOGDIR%\checkout_%1.log
)

echo ☆★☆★☆★☆★ [%date% %time%][%1] svn checkout end   ☆★☆★☆★☆★ >> %_LOGDIR%\checkout_%1.log

echo [%time%][%_BRANCH_NAME%][%1] svn checkout終了. 

shift /1
goto exectest

rem USAGE
:usage

echo usage: svnCheckOut {option} [productId] [productId] ...
echo.
echo    options:
echo       sstunk , dbmtrunk , mstrunk , pstrunk , bmctrunk , sszn , dbmzn , mszn , pszn
echo.

goto end

rem 終了
:end

endlocal

