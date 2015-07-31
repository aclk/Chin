REM entity, work, core, multi の各モジュールのブランチをエキスポートする。
REM 既にローカルに存在するバージョンはエキスポートしない。

REM SVNからのexportURL
SET SVN_TAG_ROOT=http://m6f1b013/svn/prod/spirits/co/tags/
SET SVN_TAG_ROOT_OLD=http://m6f1b013/svn/prod/tags/

REM 通常spiritsタグ
SET SVN_URL_ENTITY=%SVN_TAG_ROOT%murata-ddl/murata-ddl-%VERSION_ENTITY%/PERSONAL
SET SVN_URL_WORK=%SVN_TAG_ROOT%murata-ddl-work/murata-ddl-work-%VERSION_WORK%/PERSONAL
SET SVN_URL_CORE=%SVN_TAG_ROOT%murata-ddl-core/murata-ddl-core-%VERSION_CORE%/PERSONAL
SET SVN_URL_MULTI=%SVN_TAG_ROOT%murata-ddl-multipurposemaster/murata-ddl-multipurposemaster-%VERSION_MULTI%/PERSONAL
SET SVN_URL_SDOLI=http://m6f1b013/svn/prod/spirits/co/trunk/murata-ddl-sdoli/PERSONAL/
SET SVN_URL_SMV1370=http://m6f1b013/svn/prod/trunk/murata-ddl-euc/SS/SMV1370.sql

REM oldタグ
SET SVN_URL_ENTITY_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl/murata-ddl-%VERSION_ENTITY%/PERSONAL
SET SVN_URL_WORK_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-work/murata-ddl-work-%VERSION_WORK%/PERSONAL
SET SVN_URL_CORE_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-core/murata-ddl-core-%VERSION_CORE%/PERSONAL
SET SVN_URL_MULTI_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-multipurposemaster/murata-ddl-multipurposemaster-%VERSION_MULTI%/PERSONAL

REM entityとworkの前回バージョン(spiritsタグ)
SET OLD_SVN_URL_ENTITY=%SVN_TAG_ROOT%murata-ddl/murata-ddl-%OLD_VERSION_ENTITY%/PERSONAL
SET OLD_SVN_URL_WORK=%SVN_TAG_ROOT%murata-ddl-work/murata-ddl-work-%OLD_VERSION_WORK%/PERSONAL

REM entityとworkの前回バージョン(oldタグ)
SET OLD_SVN_URL_ENTITY_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl/murata-ddl-%OLD_VERSION_ENTITY%/PERSONAL
SET OLD_SVN_URL_WORK_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-work/murata-ddl-work-%OLD_VERSION_WORK%/PERSONAL


REM ■新Entityのエキスポート
REM SVNエキスポート(entity)
If NOT EXIST %DIR_ENTITY% svn export %SVN_URL_ENTITY% %DIR_ENTITY%
If errorlevel 1 svn export %SVN_URL_ENTITY_OLDTAG% %DIR_ENTITY%
If errorlevel 1 echo "エキスポートに失敗しました:"%VERSION_ENTITY%  >> %LOGFILE%

REM SVNエキスポート(core)
If NOT EXIST %DIR_CORE% svn export %SVN_URL_CORE% %DIR_CORE%
If errorlevel 1 svn export %SVN_URL_CORE_OLDTAG% %DIR_CORE%
If errorlevel 1 echo "エキスポートに失敗しました:"%VERSION_CORE%  >> %LOGFILE%

REM SVNエキスポート(multi)
If NOT EXIST %DIR_MULTI% svn export %SVN_URL_MULTI% %DIR_MULTI%
If errorlevel 1 svn export %SVN_URL_MULTI_OLDTAG% %DIR_MULTI%
If errorlevel 1 echo "エキスポートに失敗しました:"%VERSION_MULTI%  >> %LOGFILE%

REM SVNエキスポート(work)
If NOT EXIST %DIR_WORK% svn export %SVN_URL_WORK% %DIR_WORK%
If errorlevel 1 svn export %SVN_URL_WORK_OLDTAG% %DIR_WORK%
If errorlevel 1 echo "エキスポートに失敗しました:"%VERSION_WORK%  >> %LOGFILE%


REM ■旧Entityのエキスポート(entityとworkのみ)
REM SVNエキスポート(entity)
If NOT EXIST %OLD_DIR_ENTITY% svn export %OLD_SVN_URL_ENTITY% %OLD_DIR_ENTITY%
If errorlevel 1 svn export %OLD_SVN_URL_ENTITY_OLDTAG% %OLD_DIR_ENTITY%
If errorlevel 1 echo "エキスポートに失敗しました:"%VERSION_ENTITY%  >> %LOGFILE%

REM SVNエキスポート(work)
If NOT EXIST %OLD_DIR_WORK% svn export %OLD_SVN_URL_WORK% %OLD_DIR_WORK%
If errorlevel 1 svn export %OLD_SVN_URL_WORK_OLDTAG% %OLD_DIR_WORK%
If errorlevel 1 echo "エキスポートに失敗しました:"%VERSION_WORK%  >> %LOGFILE%

REM ■特殊SQLのエキスポート
REM SDOLIのエキスポート
If NOT EXIST %DIR_SDOLI% svn export %SVN_URL_SDOLI% %DIR_SDOLI%
If errorlevel 1 echo "エキスポートに失敗しました:"%SVN_URL_SDOLI%  >> %LOGFILE%
REM SMV1370のエキスポート
If NOT EXIST %DIR_SMV1370%\SMV1370.sql svn cat %SVN_URL_SMV1370% > %DIR_SMV1370%\SMV1370.sql
If errorlevel 1 echo "エキスポートに失敗しました:"%SVN_URL_SMV1370%  >> %LOGFILE%

