package spiritstoolkit.testutil.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import spiritstoolkit.testutil.output.ConsoleUnit;

public class TestManagerUtil {

    public static void createLaunchForTestManager(String projectName,
        String runType) throws CoreException {

        // �����`�E�}�l�[�W���̎擾
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type =
            manager
                .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
        ILaunchConfigurationWorkingCopy wc =
            type.newInstance(null, projectName + "_" + runType);

        // �v���W�F�N�g���̐ݒ�
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
            projectName);
        // ���C���E�N���X���̐ݒ�
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
            "murata.co.producttest.managers.ManualProductTestManagerImpl");
        wc.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-command " + runType);
        ILaunchConfiguration lc = wc.doSave();
        lc.launch(ILaunchManager.RUN_MODE, null);
    }

    public static void createTpFile(File file, IPath descFolderPath,
        ConsoleUnit consoleUnit) {

        if (file.getName().startsWith("TR_")) {
            // �t�H���_���쐬����
            new File(descFolderPath.toString()).mkdir();

            File descFile =
                new File(descFolderPath.append(
                    file.getName().replace("TR_", "TP_")).toString());

            try {
                consoleUnit.outPutStream("TP Creating...:" + file.getName()
                    + " >>>> " + descFile.getName());

                FileChannel sourceChannel =
                    new FileInputStream(file).getChannel();
                FileChannel destinationChannel =
                    new FileOutputStream(descFile).getChannel();

                sourceChannel.transferTo(0, sourceChannel.size(),
                    destinationChannel);

                sourceChannel.close();
                destinationChannel.close();

            } catch (Exception e) {
                // ��O�͈���Ԃ�
            }
        }
    }
}