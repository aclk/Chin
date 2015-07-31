REM ■PCごとに修正■
REM 保存先
SET DDL_DIR=D:\EntityToolPack\DDL
REM ZIP保存先
SET PACK_DIR=%DDL_DIR%\%GBASELINE_VERSION%
If NOT EXIST %PACK_DIR% MKDIR %PACK_DIR%

REM 「Entityスクリプト生成ツール.xls」のパス
SET EXCELTOOL_FILEDIR=D:\EntityToolPack
SET EXCELTOOL_FILEPATH=%EXCELTOOL_FILEDIR%\Entityスクリプト生成ツール.xls
REM 「MV→View変換マクロ.xls」のパス
SET EXCELTOOL_MV_FILEPATH=D:\EntityToolPack\MV→View変換マクロ.xls

REM 7-zipのパス
SET ZIP_PATH="C:\Program Files\7-Zip\7z"
