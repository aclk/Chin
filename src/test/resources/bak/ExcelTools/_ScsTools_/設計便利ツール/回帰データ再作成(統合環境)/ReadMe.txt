2010/03/11  21:24:59  K.Miura@SCS

��A�f�[�^���č쐬�E�㏑����@�B

-��------------------------------

�}�V��:HQA1B570
���[�U:A1B5MEMO 
�^�p:�����[�g�f�X�N�g�b�v�i�p�X���[�h�܂߁A�i�C�X�K�C�Ɠ���������ł��j

-�f�B���N�g���Q:-----------------------

D:\
��reg_reflesh	�v���_�N�g�u���āA�o�b�`���R��Ƃ���ł��B
����logs	�e����s���O���f����܂��B(�N���A���Ȃ��Ɨݐςł�)
������autotest_logs		����A�G�r�f���X��f�����߂̎����e�X�g�̃��O�u����ł��B
������rdcreate_logs		��A�f�[�^�쐬�̃��O�u����ł��B
������confirmtest_logs	�m�F�̂��߂ɍēx�s���A��A�e�X�g�̃��O�u����ł��B
������svndiff_logs	�Ō�ɂƂ�SVN�Ƃ̍������O�̒u����ł��B
����tools	���s���邽�߂̊e��c�[���������Ă��܂��B
����
�������e��v���_�N�g��
�������v���W�F�N�g�t�H���_��z�u���Ă��������B
��
��evidence	��A�f�[�^�̌��ƂȂ�A�G�r�f���X���o�͂���Ƃ���ł��B(���s�x�ɏ�����܃X)
�@��
�@�����e��v���_�N�g���̃t�H���_	������������܂�
�@�@��input	���̉�A�f�[�^(TR_)���A�R�s�[�����l�[����������(TP_)���o�͂���܂��B
�@�@��output	�V������A�f�[�^�̌��ƂȂ�G�r�f���X(TR_)���o�͂���܂��B

-�^�p���@:--------------------------

�E�قڃi�C�X�K�C�̃r���h���Ɠ��l�ł��B
�@reg_reflesh �ɁA�v���_�N�g���K�X�K�X���荞�݁A���D�݂�"x_SVN�X�V.bat"���R������A
�@"00.�J�n.bat"�Ŏ��s���Ă��������B

�E���s���������܂�����A�l�Ԍn�Ń��O�̊m�F���s���Ă��������B
�@�ĉ�A�e�X�g�ł����Ă���A�J�o���b�W���ɒ[�ɒႢ�A�Ȃǂ��m�F�A���Ǝv���܂��B

�E�m�F���I���ƁASVN�ɃR�~�b�g���Ǝv���܂��B
�@SVN�����̃��O���A�����E�}�C�R�g�ҏW������A�ꊇ�R�~�b�g�o�b�`�����邩���H

-���m�̖��-------------------------

�E�c�[���̃V�F�C�v������Ȃ�(jar���S�قǎg���d�l)
�E���ȊO�̊��œ����Ȃ��B(�h���C�u�ȂǁA���ߑł�������)
�Ecobertura��XML��f���Ȃ�(�ݒ肵�Ă���̂�)
�E�t�F�C�Y���ƂɑS�v���_�N�g���񂷌`�̂��߁A�Ō�܂ő҂��Ȃ��Ă͊m�F���ɂ����B

-���}�V���ւ̃C���X�g�[�����@-------

���O��

�ȉ��̊����������}�V�����K�v�ł��B

�Ejava1.5
�ESubversion
�EMaven2
�ESPRITS�̕W���J����(Eclispe�͕K�v�Ȃ�)

���菇

�@�u��(D�h���C�u������z��j�v�̃t�H���_�̓��e�����A
�Ώۃ}�V����D:\�����ɂ����Ă��������B

�A�u�c�[���̃\�[�X�v�t�H���_����Amaven-rdconv-plugin ��C�ӂ̃t�H���_�ɒu���A
�ȉ��̃R�}���h��@���ĉ������B

 mvn clean compile install

�B���[�J�����|�W�g��(���ʂ� [user�t�H���_]\.m2\repository)�ɂ���A
\murata\murata-app-parent �̍ŐV�o�[�W������pom(murata-app-parent-?.?.??.pom)�ɁA
�ȉ��̕�����t�������Ă��������B

��<plugins></plugins>�^�O��

		     <plugin>
		       <groupId>murata</groupId>
		       <artifactId>maven-rdconv-plugin</artifactId>
		       <version>1.0.1</version>
		     </plugin>

-----------------------------------

