2010�N7��23�� 9:25:47 K.Miura@SCS

�uSPIRITS�S��A�e�X�g�� - Linux�Łv

��What's this ?

	SPIRITS�v���W�F�N�g�ɂ�����v���O�����̍ŏ��P�ʁu�v���_�N�g�v���A�ڑ�DB�X�L�[�}���Œ肵�āA
	�C�Ӑ��A�������s�Ńe�X�g����������@�\�Ƃ��̓���E���ł��B

���O���
	
	�ȉ��̊���O��Ƃ��܂��B
	
	�EOS:Linux(��ʓI�f�B�X�g���r���[�V�����A���ۂ̓���m�F���́uOpenSuse�v)
	
	�EUTF-8���f�t�H���gLocale
	�Ebash���f�t�H���g�V�F���ł���
	�ESPIRITS�W����(�o�b�`�T�[�o���ɑ�̓����Ă������̂ł�)
		�EJava1.5�ȏ�
		�EMaven2(SPIRITS�̕W��Maven���|�W�g���Ɍq�����Ă�����)
		�Esvn(SPIRITS�̕W��SVN�T�[�o�Ɍq�����Ă�����)
	�Eruby(SS�̃T�[�o(�|�d����̖Y��`��)�ɓ����Ă����̂Łc)
	�Enkf�R�}���h
	�EJAVA_HOME��jdk�̃p�X���ݒ肳��Ă���ijre�ł͂Ȃ��j

���p�ӂ������

	�E����ReadMe.txt�����ɂ���t�@�C���Q
	
���������@

	�@spirits_dev_ss.tgz ��W�J
		
		�A�[�J�C�u��W�J���Ă��������B
		�C�ӂ̃f�B���N�g���ɂāA��A����Ɏ��s���郆�[�U��p���āA
		  tar -xvpzf spirits_dev_ss.tgz
		�œW�J��Aroot���[�U�ŁA/(���[�g�f�B���N�g��)�ւƈړ������Ă��������B
		
		���ݒu�ꏊ�A�f�B���N�g������ύX���邱�Ƃ͉\�ł��B
		���������̏ꍇ�A��̐ݒ�t�@�C���̒l�ύX���ɕύX����K�v������܂��B
		
		
	�Amvn��repository�n�ݒ�ύX
		
		�e����ˑ��̖��(�����R�[�h�Acovertura�̃o�O)�Ȃǂ����邽�߁A
		mvn���[�J�����|�W�g���̐ݒ��ύX���đΉ����܂��B
		
		�E�R���p�C���̕����R�[�h�w����͂���

			~/.m2/repository/murata/murata-parent/0.3.5/murata-parent-0.3.5.pom
		��        
			<artifactId>maven-compiler-plugin</artifactId>
		�^�O��������Aencoding�w����폜
		 
		~/.m2/repository/murata/murata-app-parent/0.3.10/murata-app-parent-0.3.10.pom
		����Asjis���ߑł��̂Ƃ�����Autf8�ɒu���B
		�������Ams932 �� utf-8 �Ɉꊇ�u���B�v4�ӏ��B

		�E�J�o���b�W�����ׂ�100%�ɂȂ�΍�

		�o�O�̋^���Z���B�������Q��
		http://d.hatena.ne.jp/taktos/20071101/1193891557
		������Q�l�ɁA
		~/.m2/repository/murata/murata-app-parent/0.3.10/murata-app-parent-0.3.10.pom
		�ɁA�����B�v�O�ӏ��B
		
		�Eprodact-test�t���[�����[�N�́u�@��ˑ����v�̉���
		
		�v���_�N�g�e�X�g�t���[�����[�N��
		�u�����p�X�̒���\(�o�b�N�X���b�V��)�����ߑł��őł���Ă���v���̉����ŁA
		  murata-co-product-test-0.4.12(�A�[�L�����̑Ή��c�̃N�Z�Ƀ��|�W�g���ɓ���Ă���Ȃ���)
		�����[�J�����|�W�g���ɒǉ�����B
		
		��murata-co-product-test-0.4.12.tgz ���A~/ (home�f�B���N�g��)�ɒu�����Ɖ���
		cd ~/.m2/repository/murata/murata-co-product-test
		mv ~/murata-co-product-test-0.4.12.tgz ./
		tar -xvzf murata-co-product-test-0.4.12.tgz
		
		
	�B��A���쐬
		
		�EDB�ڑ��ELog�̐ݒ�
		
		�@�œW�J�����Aspirits_dev_ss �� reg_daily �f�B���N�g����C�ӂ̖��O�ŃR�s�[���A
		���̒� tools/SAITO �ɂ���
		  applicationContext-datasource-local.xml
		  jdbc.properties
		  log4j.xml
		���A���s���������ւƏ���������B
		
		�E�Ώۃv���_�N�g�w��
		
		��L�A�R�s�[�����f�B���N�g������
		
		  testee.txt
		  
		�ɁA���s�������v���_�N�gID(SVN�ɂ�����v���W�F�N�g��)��񋓂���B
		
		�E����œ��������ł��B�e�X�g�Ƃ��� ./00.full.sh �����s���A
		�o�͂���� trt-result.txt ���m�F���Ă��������B


	���ԊO-�������s�ƌ��ʂ̃��[���ʒm�@�\�̕t��
		
		���K�{�ł͂���܂���B�K�v�łȂ����͖������Ă��������B
		�����[���ʒm�X�N���v�g�����s����ɂ�Ruby��Ruby-gem���K�v�ł��B������Ȃ����̕��͒��߂ĉ������B
		�����[���ʒm�X�N���v�g�͎����I�ɒ񋟂�����̂ł��B�N���鎖�̂�SCS�͈�؂̐ӔC�𕉂��܂���B
		
		�E���[���ʒm�X�N���v�g�ݒu
		
		����ReadMe.txt�Ɠ����f�B���N�g�����ɂ���Ahome_user.tgz �� ./ (home�f�B���N�g��)�ɓW�J���܂��B

		  ��home_user.tgz ���A~/ (home�f�B���N�g��)�ɒu�����Ɖ���
		  tar -xvzf home_user.tgz
		
		ss_daily_regression_kick.bsh �Ƃ����t�@�C�����W�J�����Ǝv���܂��B
		�t�@�C�����A���e��ύX���Ă��������B
		
		  1.�㕔"TARGET_MAIL_ADDRESS"�ϐ��ɂ́A���[���𑗂�A�h���X��񋓂��Ă��������B
		  2.�㕔"MAIL_SUBJECT"�ϐ��ɂ́A���[���̃^�C�g�����w�肵�Ă��������B
		  3.�㕔"TARGET"�ϐ��ɂ́A��L�@�`�B�ō쐬��������Path���w�肵�Ă��������B
		  4.����"���[���{���쐬"�R�����g�ȉ��̓��[���̖{���̓��e�ł��B���D�݂ŃJ�X�^�}�C�Y���Ă��������B
		
		�E�������s�ݒ�
		
		����ReadMe.txt�Ɠ����f�B���N�g�����ɂ���Acrontab.txt ���Q�l�ɁA
		crontab -e �R�}���h������s�ݒ��ǉ����Ă��������B
		��crontab.txt�̗�́A��L��ƂŃt�@�C������ύX���Ȃ��ꍇ�̂��̂ł��B

�����m�̖��

	�E�v���_�N�g�����烁�b�Z�[�W���\�[�X(CR9020)��ύX����悤�ȃe�X�g�f�[�^������ꍇ�A
	���̎��Ɏ��s�����v���_�N�g���猋�ʂ��O�`���O�`���ɂȂ�܂��B
	���ύX�Ȃ���Ȃ����Ƃ����҂��Ă����A���b�Z�[�W���\�[�X�����ɍs��DB�ڑ��̏��������ɑΉ����Ă��Ȃ����߁B

��Tips

	�E�x�[�X���C�����Ⴆ�Ď��s�������ꍇ
		linux���[�U��ύX���āA���[�J�����|�W�g���� murata-app-parent��pom���������Ă��������B
		�������A���s���t�@�C���Q�̃p�[�~�V�����́A�ύX�������[�U�Ŏ��s�ł���悤�ύX���Ă������ƁB
		
	�E�v���_�N�g���̃e�X�g���瑀���ǉ��E�폜�������ꍇ
		./00.full.sh ��ύX���Ă��������B
		�������f�t�H���g�ŃX�N���v�g�͂��ׂăV���{���b�N�����N�̂��߁A�R�s�[���ύX���Ă��������B

���\�z(9���ɉ��ق���邽�߁A���Ԃ񖢊��c)

	�E���m�̖��̉����B(���b�Z�[�W���\�[�X��DB�ڑ�������������悤��)
	�E���ʎ��W��������DB���A���ڍׂȁu���X�̕ω��_�v�����|�[�g����悤�@�\�ǉ��B
	�E�����d�g�݂��g���āu�����R�}���h�Ń����[�X�ł���d�g�݁v
	�E�����d�g�݂��g���āu�����R�}���h��SVN�^�O��؂�d�g�݁v