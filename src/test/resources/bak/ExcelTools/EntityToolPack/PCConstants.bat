REM ��PC���ƂɏC����
REM �ۑ���
SET DDL_DIR=D:\EntityToolPack\DDL
REM ZIP�ۑ���
SET PACK_DIR=%DDL_DIR%\%GBASELINE_VERSION%
If NOT EXIST %PACK_DIR% MKDIR %PACK_DIR%

REM �uEntity�X�N���v�g�����c�[��.xls�v�̃p�X
SET EXCELTOOL_FILEDIR=D:\EntityToolPack
SET EXCELTOOL_FILEPATH=%EXCELTOOL_FILEDIR%\Entity�X�N���v�g�����c�[��.xls
REM �uMV��View�ϊ��}�N��.xls�v�̃p�X
SET EXCELTOOL_MV_FILEPATH=D:\EntityToolPack\MV��View�ϊ��}�N��.xls

REM 7-zip�̃p�X
SET ZIP_PATH="C:\Program Files\7-Zip\7z"
