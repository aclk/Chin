OPTIONS (
	-- �_�C���N�g�E�p�X�E���[�h
	-- �}���`�X���b�f�B���O����(�_�C���N�g���[�h�̂ݗL��)
	-- �p�������ǂݍ���
	DIRECT = TRUE,
	MULTITHREADING = TRUE,
	--PARALLEL = TRUE
)
LOAD DATA
	-- ���̓t�@�C���̊����R�[�h(�L�����N�^�Z�b�g�����w��)
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
