2008�N1��30�� 17:27:18 K.Miura

MethodTracer(�v���_�N�g�e�X�gFW�����L�b�g) �g�p���@

���͂��߂�

����́ABSP�̂��g�p����v���_�N�g�EBSP�̃v���_�N�g�e�X�g���ɁABSP�̌ďo���̈����A�߂�l��
�e�X�g�P�[�X���ƂɋL�^�����邽�߂ɍ��ꂽ��@�ł���B
�܂��A�ʏ�v���_�N�g(BTP,BXP)��Editor�E�v���_�N�g�e�X�gFW�ɂ��g�p���邱�Ƃ��ł���B

���܂��m�肵�Ă��Ȃ��A2008�N5��12�����݂̎b��@�\�ł���B

�ȉ��ɓ����E�g�p���@���������B

(�T���v�����K�v�ȏꍇ�A�I�����C���Ȃ�uSOO0090�v�������ς݁B�j

���������@

1,Eclipse�̃v���W�F�N�g���Asrc/test/java �� MethodTracer.java ��ǉ����Ă��������B
MethodTracer.java�̃\�[�X�̍ŏ㕔�A���g�p�b�P�[�W�́A���g�̊��ɂ��킹�ύX���Ă��������B

2,src/test/resource/springconf/ �ɁAmethodTracer.xml ���R�s�[���Ă��������B
methodTracer.xml ���J���A�ȉ���ҏW���Ă��������B
�EMethodTracer�̃p�b�P�[�W�����A1�ŕύX�����p�b�P�[�W���ւƕύX���Ă��������B
�Eid="BspAutoProxy"��"beanNames"�v���p�e�B�ɁA���o�͂�����BSP��BMC�APSC�Ȃǂ�
�擾������Spring���DI��`��ID���L�q���Ă��������B(*Bsp�ȂǁA���C���h�J�[�h���g���܂�)
�Eid="methodTracer"��"eviOutputPath"�v���p�e�B�ɁA�t�@�C�����o�͂������f�B���N�g�����w�肵�Ă��������B

3,���g�v���W�F�N�g�́Asrc/test/resource/springconf/testBeanRefContext.xml ��ҏW���Ă��������B

���̃v���_�N�g�ł́AtestBeanRefContext.xml�̖����ɁA

	<!-- �A�v���P�[�V������`�t�@�C�� -->
	<value>murata/ss/ss/bsp/springconf/[�v���_�N�g�h�c]-bean.xml</value>
	<value>murata/ss/ss/bsp/springconf/[�v���_�N�g�h�c]-criteria.xml</value>

�̂悤�ȋL�q������Ǝv���܂����A���̏��

	<!-- ���e�X�g�p�B���O�̃C���^�[�Z�v�^�[�̎d���݁B -->
	<value>springconf/methodTracer.xml</value>

��ǉ����Ă��������B


4,src/test/java �̃e�X�g�N���X(�`Test.java or �`TestRunner.java)�ɁA

    /**
     *�e�X�g�P�[�X���̏�������
     */
    @Before
    public void setUpForMethodTracer() throws Exception {
        // �C���^�[�Z�v�^�[�p�̃t�@�C���f���N���X���������B
        try {
	        MethodTracer tracer = (MethodTracer)context.getBean("methodTracer");
	        tracer.initOutMessage("BSP�̈����E�߂�l�̋L�^�J�n�B");
        } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {}
    }
	
��ǉ����Ă��������B

��L�A�@�ŕύX�����o�͏ꏊ�ɁA�����E�߂�lVO��toString()���L�^�����A
  
  bspAndBmcParams1.log bspAndBmcParams2.log bspAndBmcParams3.log �c
  
�Ƃ����t�@�C�����ۑ�����Ă����܂��B


��MqTracer�ɂ���

MqTracer�́AMethodTracer�̓����łł���AMessagingPSC.send()���\�b�h���Ď����A
���̈����̍\������AJUnit�e�X�g�N���X�p�̊m�F�\�[�X���o�͂�����̂ł��B


��MqTracer�̓������@

��L�AMethodTracer����������Ă��邱�Ƃ��O��ł��B
methodTracer.xml �̃N���X�w��
    <bean id="methodTracer" class="murata.ss.ss.bsp.MethodTracer">
����
    <bean id="methodTracer" class="murata.ss.ss.bsp.MqTracer">
�ւƕύX���Ă��������B
�܂��ABspAutoProxy�^�O��<property name="beanNames">���X�g���ɁA
	        <value>*essagingPSC</value>
�̋L�q�����邱�Ƃ��m�F���Ă��������B
��������s����ƁAMethodTracer���̎w��o�̓t�H���_�ɁA
�V���ɁA"mqTestSrc(�ԍ�).txt"�Ƃ����e�L�X�g�t�@�C�����o�͂���܂��B

����Ə�̒���

�ʏ��MethodTracer�ɂ��o�̓t�@�C���Œl�̓��e���m�F�A
���Ȃ���΃e�X�g�N���X�̌X�P�[�X�֓\��t���Ă��������B

�E����
���m�̖��Ƃ��āA������̌ďo���s����ꍇ�ł��A�e�X�g�\�[�X�ł͕K��
	
	// �����m�F
	assertEquals(1, messages.size());
	
�Əo�͂���Ă��܂��܂��B��������MQ�ďo������ꍇ�́A
�\��t������A���ƂŕύX���Ă��������B



�����藚��

2008/07/23 K.Miura 
	����Class�����̂��̂ɑ΂��C���^�[�Z�v�g����ۂɕs����o�邽�߁A
	methodTracer.xml ��proxyTargetClass��true�w����폜�B