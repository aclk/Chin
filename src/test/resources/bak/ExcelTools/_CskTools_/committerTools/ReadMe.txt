��CommitterTooles

by CSK D.Saruhashi

�����O�ݒ�
�@- Subversion���C���X�g�[�����Ă���
�@- committerTooles�ւ�PATH��ʂ��Ă���
�@- ���p�X�L�[�}����setSchemaEnv.bat�o�b�`���́u_DB_SCHEMA�v�ɐݒ肷��
�@- SVN�A�J�E���g��setSVNEnv.bat�o�b�`���́u_SVN_USER�v�ɐݒ肷��

���ꊇ�����C���[�W
�@- �J�����g�f�B���N�g�������Ƀv���_�N�g���`�F�b�N�A�E�g����
�@- �����Ƀ`�F�b�N�A�E�g�����v���_�N�g���Ŏw�肵���v���_�N�g�ɑ΂����������{����
�@- maven test�Amaven deploy�����s����o�b�`�Ɋւ��Ă͏����O�Ƀf�[�^�\�[�X�������������s�����

���R�}���h�����ɂ���
�@�������� {option} ���w�肵���ꍇ�ASVN�擾�����ȉ��̒ʂ�ύX�����
�@
�@�@sstrunk �Fspirits/ss/trunk
�@�@pstrunk �Fspirits/ps/trunk
�@�@mstrunk �Fspirits/ms/trunk
�@�@dbmtrunk�Fspirits/dbm/trunk
�@�@bmctrunk�Fspirits/bmc/trunk
�@�@
�@�@sszn    �Fspirits/ss/branches/zn
�@�@pszn    �Fspirits/ps/branches/zn
�@�@mszn    �Fspirits/ms/branches/zn
�@�@dbmzn   �Fspirits/dbm/branches/zn
�@�@
�@�@swat    �Fbranches/swat_csk
�@�@mssys   �Fbranches/bmc-systemtest-sysm
�@�@msrun   �Fspirits/ms/branches/running

��checkout
�@> svnCheckOut {option} [productID] [productID] ...

��FileCheck�ipom.xml�Aindex.apt�j
�@> checkFiles [productID] [productID] ...

��test
�@> mvntest [productID] [productID] ...

��commands
�@> mvndeploy                        [productID] [productID] ...
�@> mvndeployForCI                   [productID] [productID] ... �iBTP-CI�Ώۃv���_�N�g�̏ꍇ��������g�p����j
�@> mvndeploy_NoTest                 [productID] [productID] ... �ino test��deploy����ꍇ�j
�@> mvndeployForCI_NoTest            [productID] [productID] ... �ino test��deploy����ꍇ(BTP-CI�Ώ�)�j
�@> mvndeploy_NoTestWithSite         [productID] [productID] ... �ino test��deploy�y��site��upload����j
�@> mvndeployForCI_NoTestWithSite    [productID] [productID] ... �ino test��deploy�y��site��upload����(BTP-CI�Ώ�)�j
�@> mvnsite                          [productID] [productID] ... �isite��upload����j
�@> mvnsite_NoTest                   [productID] [productID] ... �ino test��site��upload����j
�@> mvnsite_NoDeploy                 [productID] [productID] ... �isite���쐬�iupload�͂��Ȃ��j�j
�@> mvnsite_ForCov                   [productID] [productID] ... �idebug log���x���ɂ�site���쐬�iupload�͂��Ȃ��j�j
�@> mvnCheckoutSite                  [productID] [productID] ... �isvn checkout & mvn site�j
�@
����[OPTION]��������������������������������������������������������������������

��SVN update�i�z���̑S�v���W�F�N�g�ɑ΂���svn update�����s�j
�@> allUpdate

��SVN add tag
�@> svnAddTag {option} [productID] [version]

��SVN check tag�itag�t������Ă��邩�`�F�b�N����j
�@> svnCheckTag {option} [productID] [version]

��SVN delete tag�itag�t�����폜����j
�@> svnDelTag {option} [productID] [version]
�@
��truncaheSchema�i�w�肵���X�L�[�}�̑S�e�[�u����truncate����(���FCR9020�ADB_VERSION)�j
�@> truncaheSchema [user] [schema]

����[HISTORY]��������������������������������������������������������������������
2010/06/16 1.20 svnAddTag�C���Amvnsite_ForCov�ǉ�
2010/03/08 1.19 mvnsite_NoDeploy�AmvnCheckoutSite�ǉ�
2010/02/18 1.18 svnDelTag�ǉ��AsvnAddTag�͎��O��delete���Ă���tag�t������悤�ύX
2009/12/04 1.17 SVN�\���ύX�ɑΉ�
2009/07/21 1.16 bmctrunk�ɑΉ�
2009/07/17 1.15 swat_csk branch�ɑΉ�
2009/07/16 1.14 mvndeploy_NoTestWithSite mvndeployForCI_NoTestWithSite �ǉ�
2009/07/15 1.13 mvnsite_NoTest �ǉ�
2009/04/02 1.12 SVN checkout�Ď��s�����������������悤�C��
2009/03/13 1.11 svnAddTag��svn mkdir�Ɂu--parents�v�I�v�V������ǉ�
                mvnsite�R�}���h��ǉ�
2009/03/11 1.10 SVN�\���ύX�ɑΉ��imsrun�Amstrunk�Ή��j
2009/02/25 1.00 �����[�X


����[SPECIAL THANKS]��������������������������������������������������������������
DB�ݒ菑�������c�[���@�쐬�ҁFCSK nimota
