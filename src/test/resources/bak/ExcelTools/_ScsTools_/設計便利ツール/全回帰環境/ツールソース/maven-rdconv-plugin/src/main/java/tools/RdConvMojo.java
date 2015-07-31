package tools;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;

import murata.co.producttest.managers.ManualProductTestManager;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

/**
 * SPRITS�p�e�X�g�G�r�f���X����A�e�X�g�f�[�^�쐬�p�v���O�C��
 * @goal convert
 * @phase process-sources
 */
public class RdConvMojo extends AbstractMojo {

    /**
     * Location of the String.
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String outputDirectory;

    /**
     * Location of the String.
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private String testOutputDirectory;

    /**
     * ���s
     * {@inheritDoc}
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute() throws MojoExecutionException {

        try {

            // �p�����[�^���e�X�g�o��
            paramDebug();

            // �N���X�p�X�𑫂��Ă݂�
            addClassPath((URLClassLoader)SingletonBeanFactoryLocator.class
                .getClassLoader());

            // �R���e�L�X�g�̐���
            ApplicationContext context =
                (ApplicationContext)SingletonBeanFactoryLocator.getInstance(
                    "springconf/testBeanRefContext.xml").useBeanFactory(
                    "context").getFactory();

            // �}�j���A���e�X�g�}�l�[�W���[�𐶐�����
            ManualProductTestManager manager =
                (ManualProductTestManager)context.getBean("manualTestManager");

            // ����G�r�f���X�o�͏������s��
            manager.convert("", "");

        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }

    }

    /**
     * �Ώۃv���W�F�N�g��"/target/classes" , "/target/test-classes"�𓮓I�ɃN���X�p�X�ɒǉ�����B
     * @param classLoader �ΏۂƂȂ�N���X���[�_
     * @throws IOException
     */
    private void addClassPath(URLClassLoader classLoader) throws IOException {

        // �N���X�p�X���I�������e�B���e�B���쐬
        ClassPathModifier cpm = new ClassPathModifier(classLoader);

        // ���g���̃p�X�ǉ�(target�̎Q�Ɖ\��Class�t�H���_)
        if (outputDirectory != null) {

            super.getLog().debug((CharSequence)"classpath1:" + outputDirectory);
            cpm.addFile(outputDirectory);

            super.getLog().debug((CharSequence)"classpath2:" + testOutputDirectory);
            cpm.addFile(testOutputDirectory);

        }

    }

    /**
     * ��v�p�����[�^�̃f�o�b�O�o��
     * <br>
     * �����m�F�p�ɃV�X�e���ϐ��ȂǊm�F���������̂�Mojo�̐ݒ��log�I�u�W�F�N�g�ŏo�́B
     */
    public void paramDebug() {
        super.getLog()
            .debug((CharSequence)"outputDirectory:" + outputDirectory);
        super.getLog()
        .debug((CharSequence)"testOutputDirectory:" + testOutputDirectory);
        super.getLog().debug(
            (CharSequence)"userDir:" + System.getProperty("user.dir"));
        super.getLog().debug(
            (CharSequence)"java.class.path"
                + System.getProperty("java.class.path"));
    }

    /**
     * �Ώۃt�H���_(�R���\�[�����s or �C�ӂŎw�肵�����ꍇ�p)
     * @param outputDirectory �ΏۂƂȂ�t�H���_({project}\target)
     */
    public void setDirectory(String directory) {
        String target = directory + File.separator + "target";
        this.outputDirectory = target + File.separator + "classes";
        this.testOutputDirectory = target + File.separator + "test-classes";;
    }

    /**
     * �R���\�[�����s���̏���
     * @param args �R���\�[������
     * @throws MojoExecutionException �S�ẴG���[
     */
    public static void main(String[] args) throws MojoExecutionException {

        RdConvMojo self = new RdConvMojo();
        if (args.length > 0) {
            self.setDirectory(args[0]);
        }
        self.execute();
    }

}
