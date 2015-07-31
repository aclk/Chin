@echo off
setlocal enabledelayedexpansion
rem svn add tag�o�b�`

rem ��������
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

rem �����`�F�b�N
if "%1" == "" goto usage
if "%2" == "" goto usage

rem svn ver�`�F�b�N
set _VERCHK=
for /f "delims=" %%F in ('svn --version ^| find "�o�[�W���� 1.4"'
) do set _VERCHK=!_VERCHK! %%F

rem add tag���s
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

echo ���������������� [%date% %time%][%_PRODUCT_ID%] svn add tag start ���������������� >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log

echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn pom�擾��...
rem call ant -f %~dp0\build-svn.xml -Dbase_dir=%CD% -Dproject_name=%1 -Dmssys=true get_pom >> %_LOGDIR%\checkout_%1.log
svn cat --username %_SVN_USER% --password %_SVN_USER% %_SVN_SOURCE%/%_PRODUCT_ID%/pom.xml | more
echo ����pom.xml�ɋL�ڂ��ꂽ�o�[�W������[%_PRODUCT_VER%]�Ɠ��������m�F���āA���ł����CTRL-C�Ŗ{�o�b�`���~���ĉ�����!!����
pause

echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag start...

svn delete --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "Tag�폜 "%_PRODUCT_VER% >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
if "!_VERCHK!"=="" (
	svn mkdir --parents --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "�����[�X "%_PRODUCT_VER%                         >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
) else (
	svn mkdir --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID% -m "�����[�X "%_PRODUCT_VER%                         >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
	svn mkdir --username %_SVN_USER% --password %_SVN_USER% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "�����[�X "%_PRODUCT_VER%          >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1
)
svn copy --username %_SVN_USER% --password %_SVN_USER% %_SVN_SOURCE%/%_PRODUCT_ID% %_SVN_DEST_TAGS%/%_PRODUCT_ID%/%_PRODUCT_VER% -m "�����[�X "%_PRODUCT_VER% >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log 2>&1

if errorlevel 1 (
	echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag Failure 
) else (
	echo [!time:~0,8!][%_BRANCH_NAME%][%_PRODUCT_ID%] svn add tag Success 
	echo ����commit����tag���擾���Đ����tag�t���ł������m�F���܂�����
	call svnCheckTag %_BRANCH_NAME% %_PRODUCT_ID% %_PRODUCT_VER%
)
echo ���������������� [%date% %time%][%_PRODUCT_ID%] svn add tag end   ���������������� >> %_LOGDIR%\addtag_%_PRODUCT_ID%.log

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

rem �I��
:end

endlocal
