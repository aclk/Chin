join-test-template
�y�ł��邱�Ɓz
Oracle�X�L�[�}����e�[�u���A�J�����̘_���������������o���āA
(��)�����e�X�g�d�l���t�H�[�}�b�g�ɏ����o���Ă���܂��B

�y�g�p���@�z
�@join-test-template ��C�ӂ̃��[�J���̃t�H���_�ɃR�s�[
�Ajoin-test-template\springconf\applicationContext-datasource-local.xml ��ҏW����B

	<!-- TODO����user/password�������̃��[�U/�p�X���[�h�ɏ��������� -->
      <property name="user"                     value="fixme!!"/>
      <property name="password"                 value="fixme!!"/>

�Bjoin-test-template\springconf\applicationContext-join-test.xml ��ҏW����B
  owner�͎����̃X�L�[�}�𐄏�(�C�ӂ̃o�[�W�����ɑΉ�)�B
  tableIds �ɂ́A�K�v�ȃe�[�u�����������J���}��؂�ŗ�(���s���Ă�OK)�B

    <property name="owner" value="SCHM_M001_SCS"/>
    <property name="tableIds" value="ME0039,MR0008,ME1028,ME0009,ME1029,CR0006,MR0016,CR1004"/>

�B�fExcel�o�͐�t�H���_���쐬���ȉ��ɏo�͐��ݒ肷��B
    <property name="inputPath" value="D:\join-test-template\join-test-template\out"/>

�Cjoin-test-template\template.bat ���N��
�Djoin-test-template\out �ɏo�͂��ꂽExcel�V�[�g�����R�Ɏg�p(�蓮�e�X�g�ACriteria�L�q���̃G�r)

���C������Q��xml�t�@�C���ɓ��{��͋L�q���Ȃ��ŉ������B

