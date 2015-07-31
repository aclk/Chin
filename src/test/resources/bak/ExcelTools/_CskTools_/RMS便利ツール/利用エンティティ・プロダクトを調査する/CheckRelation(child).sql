-- �o��ON
SET SERVEROUT ON

DECLARE

	-- ���\�[�XID
	id CR9060.ID_CR9060%TYPE;
	classId CR9060.ID_CR9060%TYPE;
	-- ���\�[�X��
	resourceName CR9060.DH90301%TYPE;
	-- �G���e�B�e�BID
	entityId CR9060.DH90301%TYPE;

	-- ���ʊi�[�p��2�����z��
	TYPE Element IS TABLE OF VARCHAR2(1000) INDEX BY BINARY_INTEGER;
	TYPE Square IS TABLE OF Element INDEX BY BINARY_INTEGER;
	Relation Square;
	counter Number(10);
	checker Number(10);
	flg Number(1);

	FUNCTION isAlready(resourceId IN CR9060.ID_CR9060%TYPE) RETURN INTEGER IS
	BEGIN
		checker := 1;
		WHILE Relation.exists(checker) LOOP
			IF (Relation(checker)(1) = resourceId) THEN
			        return 1;
			END IF;
			checker := checker + 1;
		END LOOP;
	        return 0;
	END;

	-- �T�u�v���O����
	-- �����œn���ꂽ��񂪔z��ɒ~�ς���Ă��Ȃ��ꍇ�́A�z��ɉ�����
	PROCEDURE printList IS
	BEGIN
		checker := 1;
		WHILE checker < counter LOOP
			IF Relation(checker)(2) = 61 THEN
				SELECT TO_CHAR(dh90305) INTO entityId FROM CR9062 WHERE ID_CR9060 = Relation(checker)(1) AND ID_CR9063 = 401;
				DBMS_OUTPUT.PUT_LINE(Relation(checker)(1) || ' ' || Relation(checker)(2) || ' ' || Relation(checker)(3) || ' ' || entityId);
			END IF;
			checker := checker + 1;
		END LOOP;
	END;

	-- �����œn���ꂽ��񂪔z��ɒ~�ς���Ă��Ȃ��ꍇ�́A�z��ɉ�����
	PROCEDURE addRelationList ( resourceId IN CR9060.ID_CR9060%TYPE, resourceType IN CR9061.ID_CR9061%TYPE, name IN VARCHAR2) IS
	BEGIN
		checker := 1;
		flg := 0;

		WHILE Relation.exists(checker) LOOP
			IF (Relation(checker)(1) = resourceId) THEN
				EXIT;
			END IF;
			checker := checker + 1;
		END LOOP;

		Relation(counter)(1) := resourceId;
		Relation(counter)(2) := resourceType;
		Relation(counter)(3) := name;
		counter := counter + 1;
	END;

	-- �ċA�I�ɌĂяo����ABMC�ABSP�̊֘A�������Ȃ�܂ŌJ��Ԃ�
	PROCEDURE getRelation ( resourceId IN CR9060.ID_CR9060%TYPE, mark IN VARCHAR2) IS
	BEGIN

		-- �w�肳�ꂽ���\�[�XID�����p����BMC�ABSP���擾����
		FOR RECORD IN (
			SELECT	TO_CHAR(REL.ID_CR9060_CHILD, '000000') RESID,
					RES.ID_CR9061 RES_TYPE,
					RES.DH90301 R_NAME
			FROM CR9064 REL
			INNER JOIN CR9060 RES ON REL.ID_CR9060_CHILD = RES.ID_CR9060
			WHERE REL.ID_CR9060_PARENT = resourceId
			AND ID_CR9061 IN (12, 21, 52, 61)) LOOP

			DBMS_OUTPUT.PUT_LINE(RECORD.RESID || '  ' || mark || '  ' || RECORD.R_NAME);
			IF isAlready(RECORD.RESID) = 0 THEN
				addRelationList(RECORD.RESID, RECORD.RES_TYPE, RECORD.R_NAME);
				
				IF RECORD.RES_TYPE = 12 OR RECORD.RES_TYPE = 21 OR RECORD.RES_TYPE = 52 THEN
					-- �ċA�Ăяo��
					getRelation(RECORD.RESID, '-' || mark);
				END IF;
			END IF;
		END LOOP;
	END;

BEGIN

	-- ���O�ݒ�
	-- �R���\�[���o�͂��ő��
	DBMS_OUTPUT.ENABLE(NULL);
	-- �J�E���^�̏�����
	counter := 1;

	-- ���\�[�XID��ݒ�
	id := &Num;
	SELECT DH90301 INTO resourceName FROM CR9060 WHERE ID_CR9060 = id;
	DBMS_OUTPUT.NEW_LINE;
	DBMS_OUTPUT.PUT_LINE('�����Ώ� [ID:' || id || ']' || resourceName);
	DBMS_OUTPUT.NEW_LINE;

	DBMS_OUTPUT.PUT_LINE('���������������������֘AEntity���� �J�n��������������������');

	-- ���p���\�[�X�̒����J�n
	getRelation(id, '+');

	DBMS_OUTPUT.PUT_LINE('���������������������֘AEntity���� ������������������������');

	-- ���㏈��
	printList();

EXCEPTION

	WHEN OTHERS THEN
	RAISE;

END;
/
