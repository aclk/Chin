2009�N9��4�� 17:57:46 SCS�O�Y��m

P6Spy��Log4j�̎g�����Ƃ��Ắu���O�ɍ������ė~�����Ȃ�����A����t�@�C���ɓZ�߂ď����o�������v���Ǝv���܂��B
�����ŁAP6spy�ƁA���ł�log4j�ŁA�u����t�@�C���ɓf���ݒ��v���Љ�܂��B

�EP6Spy�̐ݒ���@

1.spy.properties�t�@�C�����A
[�v���W�F�N�g�t�H���_]\src\main\resources\
�ɏ㏑���R�s�[���܂��B

2.�o�̓t�@�C���̃p�X�ȂǁA�ݒ�������F�ɐ��߂܂��B

3.�f�[�^�x�[�X�̐ڑ��t�@�C��
[�v���W�F�N�g�t�H���_]\src\main\resources\springconf\applicationContext-datasource-local.xml
���A�ȉ��̂悤�ɕύX���܂��B

		<property name="driverClass" value="oracle.jdbc.driver.OracleDriver" />		�����������E����
		<!--
		<property name="driverClass" value="com.p6spy.engine.spy.P6SpyDriver"/>		���������𐶂���
		 -->
		 

�ELog4j�̐ݒ���@

Wiki�Q��