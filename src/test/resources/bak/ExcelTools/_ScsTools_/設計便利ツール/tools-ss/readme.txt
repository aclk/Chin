�ytools-ss�z
ver1.0 2008/5/20 SCS�匴
ver1.1 2008/6/13 SCS�匴	MessageCopy��ǉ�

���܂łɍ쐬���ꂽ�c�[���ނ̊񂹏W�߂ł��B
���ł��邱��
1.CriteriaEvidenceCreator			Criteria�̃G�r�Ƃ�Ɏg�p�B
2.CriteriaEvidenceCreatorAdaptor	�ݒ�t�@�C���̎w�肪�s�v�ɂȂ�܂��B
3.TrialImpl							BMC�Ȃǂ̎��p���ł��܂��B�e�X�g�f�[�^�쐬���Ȃǂɗ��p�B
4.LineCounter						�w�肵���t�H���_�ȉ��̃v���W�F�N�g�ɂ���Criteria�̍s���𐔂���B
5.MessageCopy						build.jcl���g�킸�ɁARMS�̃��b�Z�[�W���R�s�[����

���������@
Eclipse�p�b�P�[�W�G�N�X�v���[����
�E�N���b�N > �C���|�[�g > �u��ʁv > �u�����v���_�N�g�����[�N�X�y�[�X�ցv
�u�A�[�J�C�u�t�@�C���̑I���v

�ȉ��t�@�C���̃��[�U��/�p�X���[�h/(�X�L�[�}��) �������̂��̂ɕҏW
/src/main/resources/springconf/applicationContext-datasource-local.xml
/src/main/resources/jdbc.properties
/src/main/resources/input.txt 1�s�ڂɃX�L�[�}��

�����
1.CriteriaEvidenceCreator(CEC)
��Javadoc�ɂ�������ǂ�ł��������B
  �e�v���W�F�N�g�փR�s�[����K�v�͂���܂���B
  ���s���@
  �\�[�X�t�@�C���E�N���b�N�� ���s��Java�A�v���P�[�V����

2.CriteriaEvidenceCreatorAdaptor
CEC��֗��ɗ��p���邽�߂̃A�_�v�^�ł��B
-Main�Ł�CriteriaEvidenceCreator�̉���Ɠ����ł��B
 input.txt�t�@�C�������A�ꏊ�Œ�ɂȂ�܂�(src/main/resources/input.txt)
 �\�[�X�t�@�C���E�N���b�N�� ���s��Java�A�v���P�[�V����

-API��
 ���ʂ̐l�͂��܂藘�p���Ȃ��ł��E�E�E
 ��q��TraialImpl���ŗ��p���Ă��܂��B

3.TrialImpl
build.jcl ���N�����邱�ƂŁA
execute()���\�b�h���Ăяo���܂��B
���e�͊e���D���Ȃ悤�ɕҏW���Ă��������B�ȉ��̂��Ƃ��\�ł��B
�EDAC�̊e���\�b�h���s
�ECriteria���s
�EDB��������(�g�����U�N�V�����Ή�)
�EBMC/PSC�Ăяo��
�E�����̏����̃��O�o��
�E���O�����ł̃f�[�^����(CriteriaEvidenceCreator API�ŗ��p)


4.LineCounter
��Javadoc�ɂ�������ǂ�ł��������B
 ���ʂ̐l�͂��܂藘�p���Ȃ��ł��B

5.MessageCopy
���s>Java�A�v���P�[�V����
�v���O����������"diff"�܂���"all"���w�肵�Ď��s����B
(all��10���قǂ�����܂�)

��TIPS
TrialImpl��
BMC��inputVO�EoutputVO�������I�Ƀ��O�o�͂����ݒ�

log4j.xml �ňȉ��̋L�q���R�����g�O��

  <category name="murata.co.inspection.BMCLoggingAspect">
      <priority value="DEBUG" />
  </category>

������
�E�ˑ��惉�C�u������pom.xml�ɂ��̂ŁA���ӂ��K�v�ł�(�f�t�H���g�ł�murata-ss-sa�̍ŐV)
�E���̃v���W�F�N�g�͏�����ɂ��svn�o�^�ł��܂���B
  �o�O�t�B�b�N�X�Ȃǂ́A�T�[�o�[���zip���X�V���Ă����\��ł��B