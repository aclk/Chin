�E�V�K�쐬�@�~�c����
�E�����ǉ��@�O�c(��)
�E2009/11/05�@�O�c(��) �����ǉ�(MethodTracer���g�p���Ă���ꍇ)
�E2009/11/05�@�O�c(��) Log4j.xml��CloseCheckAspect���ǉ�
�E2010/03/28�@����     �o�b�`���scloseCheck.bat�ǉ�

====================================================================================================
�y�Ή����@�z�i�o�b�`�őΉ�����ꍇ�j
�@���菇
�@�@�P�DcloseCheck.bat���́u_DB_SCHEMA�v�𗘗p����X�L�[�}�֏���������
�@�@�@�@���ڑ���C���X�^���X�́uDB�ݒ菑������\dbSetting.properties�v��"jdbc.url.0"�Œ�`���ꂽ�C���X�^���X���w�肳���
�@�@�Q�DcloseChecker��PATH��ʂ��i���������ɂȂ�̂ł���PATH��ɓ��{�ꂪ���݂��Ȃ��悤�ɂ��ĉ������j
�@�@�R�D�Ώۃv���_�N�g���`�F�b�N�A�E�g
�@�@�S�D�Ώۃv���_�N�g���`�F�b�N�A�E�g�����t�H���_�� closeCheck.bat [productID] �����s�i��A�e�X�g������܂��j
�@�@�@�@ex) closeCheck sjo0010
�@�@�@�@�@�@���v���_�N�gID�͕����w��\�icloseCheck sjo0010 sjo0020 sjo0030�E�E�E�j
�@
�@���⑫
�@�@closeCheck.bat�ł� pom.xml�Alog4j.xml�����������Ă���܂��B
�@�@���s���͌��t�@�C���� "*.bak" �t�@�C���ɑޔ����Ă��܂����A�o�b�`���s��r���ŋ����I�������ꍇ�́A
�@�@cleanupForCloseCheck.bat �����s����� "*.bak" �t�@�C���� ���̃t�@�C���֖߂��܂��B


�y���ӎ����z
�@���Ώۃv���_�N�g��MethodTracer���g�p���Ă���ꍇ�A�{�Ή����s�Ȃ��ƃG���[�ɂȂ��Ă��܂��B
�@�@�@java.lang.ExceptionInInitializerError�@�ŉ�A��1�������Ȃ�
�@�@��
�@�@���΍�
�@�@��
�@�@testBeanRefContext.xml�@�́@<value>springconf/methodTracer.xml</value>�@���R�����g�ɂ���

-----------------------------------------------------------------------------------------------------------------
�yclose�R��̉\��������ꍇ�́@�o�͗�z
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:56) - Close�`�F�b�N�G�[�W�F���g�N��
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:78) - ���j�^���\�b�h=null
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:79) - ���j�^���\�b�h(�s)=null
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:80) - �^�[�Q�b�g=murata.ss.sx.sxb2015.editor.*
	2008-11-27 13:57:05,505 INFO  [main] (CloseCheckAgent.java:90) - JVM�I�����t�b�N
	2008-11-27 13:57:09,734 INFO  [main] (CloseCheckAgent.java:117) - �y�ÓI�����z���͂��܂��B:murata.ss.sx.sxb2015.editor.Sxb2015EditorImpl
	2008-11-27 13:57:09,844 INFO  [main] (CloseCheckAgent.java:182) - �y�ÓI�����zExecutor�����̎��������o�F�������@=prepareBatchInsert / �t�@�C��=Sxb2015EditorImpl.java / �s��=231
	2008-11-27 13:57:09,969 INFO  [main] (CloseCheckAgent.java:164) - �y�ÓI�����zQuery�����̎��������o�F�������@=query / �t�@�C��=Sxb2015EditorImpl.java / �s��=255
	2008-11-27 13:57:09,985 INFO  [main] (CloseCheckAgent.java:164) - �y�ÓI�����zQuery�����̎��������o�F�������@=queryForFill / �t�@�C��=Sxb2015EditorImpl.java / �s��=283
	2008-11-27 13:57:10,000 INFO  [main] (CloseCheckAgent.java:164) - �y�ÓI�����zQuery�����̎��������o�F�������@=queryForFill / �t�@�C��=Sxb2015EditorImpl.java / �s��=342
	2008-11-27 13:57:10,000 INFO  [main] (CloseCheckAgent.java:164) - �y�ÓI�����zQuery�����̎��������o�F�������@=queryForFill / �t�@�C��=Sxb2015EditorImpl.java / �s��=382
	2008-11-27 13:57:10,000 INFO  [main] (CloseCheckAgent.java:224) - �y�ÓI�����z�g�����܂��B:murata.ss.sx.sxb2015.editor.Sxb2015EditorImpl
	2008-11-27 13:57:10,016 INFO  [main] (CloseCheckAgent.java:117) - �y�ÓI�����z���͂��܂��B:murata.ss.sx.sxb2015.editor.Sxb2015Editor
	2008-11-27 13:57:10,016 INFO  [main] (CloseCheckAgent.java:224) - �y�ÓI�����z�g�����܂��B:murata.ss.sx.sxb2015.editor.Sxb2015Editor

	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:271) - ������CLOSE��ԃ��j�^�[�i���̃e�X�g�P�[�X�̌��ʂ��܂݂܂��j������
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=342
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283
	2008-11-27 13:57:30,035 INFO  [Thread-0] (CloseCheckAgent.java:274) - �^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=382


�ǂݕ�
	close()���\�b�h���Ă΂�Ă��Ȃ�Query/Cursor/Executor�̐����ʒu�����o�́B

	�^�킵���E�E�E[Query] fileName=Sxb2015EditorImpl.java / lineNumber=283