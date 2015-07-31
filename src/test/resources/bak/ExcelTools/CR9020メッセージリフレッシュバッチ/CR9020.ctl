OPTIONS (
	-- ダイレクト・パス・ロード
	-- マルチスレッディング処理(ダイレクトモードのみ有効)
	-- パラレル読み込み
	DIRECT = TRUE,
	MULTITHREADING = TRUE,
	--PARALLEL = TRUE
)
LOAD DATA
	-- 入力ファイルの漢字コード(キャラクタセット名を指定)
	CHARACTERSET JA16SJISTILDE
	-- DATA files , BAD , DISC file
	-- INFILE       'MESSAGE.dat'
	BADFILE      'MESSAGE.bad'
	DISCARDFILE  'MESSAGE.dis'
	-- APPEND ROWS
	TRUNCATE INTO TABLE CR9020
	(
		"ID90400" POSITION(1:256) ,
		"DH90400" POSITION(257:2256) "NVL(:DH90400, ' ')",
		"DH90401" POSITION(2257:4257) "NVL(:DH90401, ' ')",
		"VALID_FROM" ,
		"VALID_TO" ,
		"PMS_I_YMD" SYSDATE,
		"PMS_I_USR" CONSTANT 'messageRefresh',
		"PMS_I_CLASS",
		"PMS_U_YMD" SYSDATE,
		"PMS_U_USR" CONSTANT 'messageRefresh',
		"PMS_U_CLASS",
		"VERSION" "0"
)
