2010�N9��24�� 10:05:31 K.Miura@SCS

�uSPIRITS����DB�X�L�[�}�ŐV���X�N���v�g�v

��What's this ?

	SPIRITS�v���W�F�N�g�ɂ����āA���X�X�V�����v���_�N�g�p�x�[�X���C��[murata-ps-baseline]�̍ŐV����A
	�R�Â�[murata-entitiy-baseline]������o���A�������炳��ɕR�t��Entity�c����R�t��DDL���擾�A
	�w�肵���T�[�o�E�X�L�[�}�Ɏ����I�ɓK�p����A���̋@�\�Ƃ��̓���E���ł��B

���O���
	
	�ȉ��̊���O��Ƃ��܂��B
	
	�EOS:Linux(��ʓI�f�B�X�g���r���[�V�����A���ۂ̓���m�F���́uOpenSuse�v)
	����L�ȊO�ɂ��A�ȉ��Ɏ������������Ă���Unix/Solaris/BSD�Ȃ�A���삷��\������B
		
	�E�ȉ��̃\�t�g�E�F�A����������Ă���
		�EUTF-8���f�t�H���gLocale
		�Ebash���f�t�H���g�V�F���ł���
		�Esvn(SPIRITS�̕W��SVN�T�[�o�Ɍq�����Ă�����)
		�Esqlplus(�ΏۃT�[�o�ɐڑ��\�Atnsnames.ora�Ȃǂ��������ꂽ���)
		�Eruby
		�Enkf�R�}���h

���p�ӂ������

	�E����ReadMe.txt
	�E�����ɂ���t�@�C���Q
		spirits_dev_ps.tgz
		home_user.tgz
		crontab.txt

���\��
	
	[�C�ӂ̃f�B���N�g��]/spirits_dev_ps
		��scripts	�� �X�N���v�g�̖{�̂��i�[�B
		����ringmail.rb	�����[�����M�p�X�N���v�g�Bruby���B
		��ddl	�����s�f�B���N�N�g���B�X�N���v�g�Q�͏�Lscripts����̃V���{���b�N�����N
		����get_bl_ver_ddl.bsh
		����get_latest_ddl.bsh
		����all_ddl_reflash.bsh
		��logs	�����s���̃��O�Ǝg���I�����SQL�̎c�[�𒙂߂�f�B���N�g���B
		
	[���s���[�U��home�f�B���N�g��]
		��weekly_dbschema_update.bsh
	
	
	���X�N���v�g����
	
		get_bl_ver_ddl.bsh
			�����Ɏw�肵��murata-entity-baselne�̃o�[�W�����ɕR�Â�DDL(����Awork�A�ėpT)��
			SVN�R�}���h��/tag����_�E�����[�h���ꏊ�ɂ܂Ƃ߂�X�N���v�g�B
			
		get_latest_ddl.bsh
			murata-ps-baseline��site��"�v���W�F�N�g�̈ˑ��֌W"�y�[�W����A
			murata-entity-baseline�̃o�[�W�����𒲂ׁA
			����������ɏ�Lget_bl_ver_ddl.bsh�����s����X�N���v�g�B
			
		all_ddl_reflash.bsh
			�����Ɏw�肳�ꂽsqlplus�ڑ��p����(user/pass@tns)���g���A
			�J�����g�f�B���N�g�����ɂ��� "murata-ddl*/ReplaceAllObjects.sql" ��
			���ׂĘA���Ŏ��s����X�N���v�g�B
			
		weekly_dbschema_update.bsh
			get_latest_ddl.bsh�Aall_ddl_reflash.bsh �����ԂɎ��s���A�����チ�[���𑗐M����X�N���v�g�B
			�Œ�ŕ�����sqlplus�ڑ��p����(user/pass@tns)�A�������[���A�h���X���w��\�B
			�R�s�[�E�J�X�^�}�C�Y���Ďg�p����B
		
���������@

	�@spirits_dev_ps.tgz ��W�J
		
		�A�[�J�C�u��W�J���Ă��������B
		�C�ӂ̃f�B���N�g���ɂāA���s���郆�[�U��p���āA
		  tar -xvpzf spirits_dev_ps.tgz
		�œW�J��Aroot���[�U�ŁA/(���[�g�f�B���N�g��)�ւƈړ������Ă��������B
		
		���ݒu�ꏊ�A�f�B���N�g������ύX���邱�Ƃ͉\�ł��B
		���������̏ꍇ�A��̐ݒ�t�@�C���̒l�ύX���ɕύX����K�v������܂��B


	�A�������s�ƌ��ʂ̃��[���ʒm�X�N���v�g��z�u
		
		����ReadMe�Ɠ����f�B���N�g�����ɂ���Ahome_user.tgz �� ~/ (home�f�B���N�g��)�ɓW�J���܂��B

		  ��home_user.tgz ���A~/ (home�f�B���N�g��)�ɒu�����Ɖ���
		  tar -xvzf home_user.tgz
		
		weekly_dbschema_update.bsh �Ƃ����t�@�C�����W�J�����Ǝv���܂��B
		�t�@�C�����A���e��ύX���Ă��������B
		
		  1.�㕔"TARGET_MAIL_ADDRESS"�ϐ��ɂ́A���[���𑗂�A�h���X��񋓂��Ă��������B
		  	" "(���p�X�y�[�X)�A���s�ATab�ŋ�؂�A�����w�肷�邱�Ƃ��\�ł��B
		  2.�㕔"MAIL_SUBJECT"�ϐ��ɂ́A���[���̃^�C�g�����w�肵�Ă��������B
		  3.�㕔"TARGET_SC"�ϐ��ɂ́Asalplus�R�}���h�̐ڑ��p����(user/pass@tns)��񋓂��Ă��������B
		  	" "(���p�X�y�[�X)�A���s�ATab�ŋ�؂�A�����w�肷�邱�Ƃ��\�ł��B
		  4.����"���[���{���쐬"�R�����g�ȉ��̓��[���̖{���̓��e�ł��B���D�݂ŃJ�X�^�}�C�Y���Ă��������B
		  
		  �����p�^�[�����s�������ꍇ�A�R�s�[�E���l�[�������ꂼ��ʐݒ�ɂ��Ă��������B
		
	�B�������s�ݒ�
		
		����ReadMe�Ɠ����f�B���N�g�����ɂ���Acrontab.txt ���Q�l�ɁA
		crontab -e �R�}���h������s�ݒ��ǉ����Ă��������B
		(weekly_dbschema_update.bsh ���܂܂��s���K�v�ȍs�ł�)
		��crontab.txt�̗�́A��L��ƂŃt�@�C������ύX���Ȃ��ꍇ�̂��̂ł��B

�����m�̖��

	�E�G���[���ڂ������肷��@�\���s�����Ă��܂��B
	�@NG��ʒm���Ă����̂́uDDL�����Ȃ��������̂݁v�ŁA
	�@�u�X��DDL���s�̎��s�v�͍l�����܂���B(OK�Œʒm���܂�)

