rem SVNä÷åWä¬ã´ê›íË


set _SVN_REMOTE_URL=svn://m6f1b013/prod
rem set _SVN_REMOTE_URL=http://hq1b216/svn/dev
set _SVN_TRUNK=trunk
set _SVN_TAGS=tags
set _SVN_MS=spirits/ms
set _SVN_SS=spirits/ss
set _SVN_PS=spirits/ps
set _SVN_DBM=spirits/dbm
set _SVN_BMC=spirits/bmc

rem mstrunk
set _SVN_MS_TRUNK=%_SVN_MS%/%_SVN_TRUNK%
set _SVN_MS_TAGS=%_SVN_MS%/%_SVN_TAGS%
rem sstrunk
set _SVN_SS_TRUNK=%_SVN_SS%/%_SVN_TRUNK%
set _SVN_SS_TAGS=%_SVN_SS%/%_SVN_TAGS%
rem pstrunk
set _SVN_PS_TRUNK=%_SVN_PS%/%_SVN_TRUNK%
set _SVN_PS_TAGS=%_SVN_PS%/%_SVN_TAGS%
rem dbmtrunk
set _SVN_DBM_TRUNK=%_SVN_DBM%/%_SVN_TRUNK%
set _SVN_DBM_TAGS=%_SVN_DBM%/%_SVN_TAGS%
rem bmctrunk
set _SVN_BMC_TRUNK=%_SVN_BMC%/%_SVN_TRUNK%
set _SVN_BMC_TAGS=%_SVN_BMC%/%_SVN_TAGS%
rem branch
set _SVN_MSSYS_BRANCH=branches/bmc-systemtest-sysm
set _SVN_MSRUN_BRANCH=%_SVN_MS%/branches/running
set _SVN_SSZN_BRANCH=%_SVN_SS%/branches/zn
set _SVN_PSZN_BRANCH=%_SVN_PS%/branches/zn
set _SVN_DBMZN_BRANCH=%_SVN_DBM%/branches/zn
set _SVN_SWAT_BRANCH=branches/swat_csk
set _SVN_USER=A1M4MEM075

if "%1" == "mssys" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/branches/bmc-systemtest-sysm
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_MSSYS_BRANCH%
	rem svn://m6f1b013/prod/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "msrun" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ms/branches/running
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_MSRUN_BRANCH%
	rem svn://m6f1b013/prod/spirits/ms/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_MS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "mstrunk" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ms/trunk
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_MS_TRUNK%
	rem svn://m6f1b013/prod/spirits/ms/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_MS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "sstrunk" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ss/trunk
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_SS_TRUNK%
	rem svn://m6f1b013/prod/spirits/ss/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_SS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "pstrunk" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ps/trunk
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_PS_TRUNK%
	rem svn://m6f1b013/prod/spirits/ps/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_PS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "dbmtrunk" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/dbm/trunk
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_DBM_TRUNK%
	rem svn://m6f1b013/prod/spirits/dbm/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_DBM_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3

) else if "%1" == "mszn" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ms/branches/zn
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_MSZN_BRANCH%
	rem svn://m6f1b013/prod/spirits/ms/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_MS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "sszn" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ss/branches/zn
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_SSZN_BRANCH%
	rem svn://m6f1b013/prod/spirits/ss/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_SS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "pszn" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/ps/branches/zn
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_PSZN_BRANCH%
	rem svn://m6f1b013/prod/spirits/ps/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_PS_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "dbmzn" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/dbm/branches/zn
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_DBMZN_BRANCH%
	rem svn://m6f1b013/prod/spirits/dbm/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_DBM_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3

) else if "%1" == "swat" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/branches/swat_csk
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_SWAT_BRANCH%
	rem svn://m6f1b013/prod/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
) else if "%1" == "bmctrunk" (
	set _BRANCH_NAME=%1
	rem svn://m6f1b013/prod/spirits/bmc/trunk
	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_BMC_TRUNK%
	rem svn://m6f1b013/prod/spirits/bmc/tags
	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_BMC_TAGS%
	set _PRODUCT_ID=%2
	set _PRODUCT_VER=%3
	
) else (
rem 	rem svn://m6f1b013/prod/trunk
rem 	set _SVN_SOURCE=%_SVN_REMOTE_URL%/%_SVN_TRUNK%
rem 	rem svn://m6f1b013/prod/tags
rem 	set _SVN_DEST_TAGS=%_SVN_REMOTE_URL%/%_SVN_TAGS%
rem 	set _PRODUCT_ID=%1
rem 	set _PRODUCT_VER=%2
rem trunkÇÕégópïsâ¬
echo [ERROR!!] SVN path ñ¢ê›íË
pause
exit /b 1
)

exit /b 0
