REM entity, work, core, multi �̊e���W���[���̃u�����`���G�L�X�|�[�g����B
REM ���Ƀ��[�J���ɑ��݂���o�[�W�����̓G�L�X�|�[�g���Ȃ��B

REM SVN�����exportURL
SET SVN_TAG_ROOT=http://m6f1b013/svn/prod/spirits/co/tags/
SET SVN_TAG_ROOT_OLD=http://m6f1b013/svn/prod/tags/

REM �ʏ�spirits�^�O
SET SVN_URL_ENTITY=%SVN_TAG_ROOT%murata-ddl/murata-ddl-%VERSION_ENTITY%/PERSONAL
SET SVN_URL_WORK=%SVN_TAG_ROOT%murata-ddl-work/murata-ddl-work-%VERSION_WORK%/PERSONAL
SET SVN_URL_CORE=%SVN_TAG_ROOT%murata-ddl-core/murata-ddl-core-%VERSION_CORE%/PERSONAL
SET SVN_URL_MULTI=%SVN_TAG_ROOT%murata-ddl-multipurposemaster/murata-ddl-multipurposemaster-%VERSION_MULTI%/PERSONAL
SET SVN_URL_SDOLI=http://m6f1b013/svn/prod/spirits/co/trunk/murata-ddl-sdoli/PERSONAL/
SET SVN_URL_SMV1370=http://m6f1b013/svn/prod/trunk/murata-ddl-euc/SS/SMV1370.sql

REM old�^�O
SET SVN_URL_ENTITY_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl/murata-ddl-%VERSION_ENTITY%/PERSONAL
SET SVN_URL_WORK_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-work/murata-ddl-work-%VERSION_WORK%/PERSONAL
SET SVN_URL_CORE_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-core/murata-ddl-core-%VERSION_CORE%/PERSONAL
SET SVN_URL_MULTI_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-multipurposemaster/murata-ddl-multipurposemaster-%VERSION_MULTI%/PERSONAL

REM entity��work�̑O��o�[�W����(spirits�^�O)
SET OLD_SVN_URL_ENTITY=%SVN_TAG_ROOT%murata-ddl/murata-ddl-%OLD_VERSION_ENTITY%/PERSONAL
SET OLD_SVN_URL_WORK=%SVN_TAG_ROOT%murata-ddl-work/murata-ddl-work-%OLD_VERSION_WORK%/PERSONAL

REM entity��work�̑O��o�[�W����(old�^�O)
SET OLD_SVN_URL_ENTITY_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl/murata-ddl-%OLD_VERSION_ENTITY%/PERSONAL
SET OLD_SVN_URL_WORK_OLDTAG=%SVN_TAG_ROOT_OLD%murata-ddl-work/murata-ddl-work-%OLD_VERSION_WORK%/PERSONAL


REM ���VEntity�̃G�L�X�|�[�g
REM SVN�G�L�X�|�[�g(entity)
If NOT EXIST %DIR_ENTITY% svn export %SVN_URL_ENTITY% %DIR_ENTITY%
If errorlevel 1 svn export %SVN_URL_ENTITY_OLDTAG% %DIR_ENTITY%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%VERSION_ENTITY%  >> %LOGFILE%

REM SVN�G�L�X�|�[�g(core)
If NOT EXIST %DIR_CORE% svn export %SVN_URL_CORE% %DIR_CORE%
If errorlevel 1 svn export %SVN_URL_CORE_OLDTAG% %DIR_CORE%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%VERSION_CORE%  >> %LOGFILE%

REM SVN�G�L�X�|�[�g(multi)
If NOT EXIST %DIR_MULTI% svn export %SVN_URL_MULTI% %DIR_MULTI%
If errorlevel 1 svn export %SVN_URL_MULTI_OLDTAG% %DIR_MULTI%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%VERSION_MULTI%  >> %LOGFILE%

REM SVN�G�L�X�|�[�g(work)
If NOT EXIST %DIR_WORK% svn export %SVN_URL_WORK% %DIR_WORK%
If errorlevel 1 svn export %SVN_URL_WORK_OLDTAG% %DIR_WORK%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%VERSION_WORK%  >> %LOGFILE%


REM ����Entity�̃G�L�X�|�[�g(entity��work�̂�)
REM SVN�G�L�X�|�[�g(entity)
If NOT EXIST %OLD_DIR_ENTITY% svn export %OLD_SVN_URL_ENTITY% %OLD_DIR_ENTITY%
If errorlevel 1 svn export %OLD_SVN_URL_ENTITY_OLDTAG% %OLD_DIR_ENTITY%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%VERSION_ENTITY%  >> %LOGFILE%

REM SVN�G�L�X�|�[�g(work)
If NOT EXIST %OLD_DIR_WORK% svn export %OLD_SVN_URL_WORK% %OLD_DIR_WORK%
If errorlevel 1 svn export %OLD_SVN_URL_WORK_OLDTAG% %OLD_DIR_WORK%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%VERSION_WORK%  >> %LOGFILE%

REM ������SQL�̃G�L�X�|�[�g
REM SDOLI�̃G�L�X�|�[�g
If NOT EXIST %DIR_SDOLI% svn export %SVN_URL_SDOLI% %DIR_SDOLI%
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%SVN_URL_SDOLI%  >> %LOGFILE%
REM SMV1370�̃G�L�X�|�[�g
If NOT EXIST %DIR_SMV1370%\SMV1370.sql svn cat %SVN_URL_SMV1370% > %DIR_SMV1370%\SMV1370.sql
If errorlevel 1 echo "�G�L�X�|�[�g�Ɏ��s���܂���:"%SVN_URL_SMV1370%  >> %LOGFILE%

