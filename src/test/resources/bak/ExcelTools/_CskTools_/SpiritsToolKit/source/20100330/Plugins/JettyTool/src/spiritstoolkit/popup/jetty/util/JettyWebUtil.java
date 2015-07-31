package spiritstoolkit.popup.jetty.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import spiritstoolkit.popup.jetty.output.ConsoleUnit;

import com.iw.plugins.jetty.JettyPlugin;

public class JettyWebUtil {

    public static void createLaunchForJetty(String projectName, String runType,
        IProject project) throws CoreException {

        // �����`�E�}�l�[�W���̎擾
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();

        ILaunchConfiguration[] lcs = manager.getLaunchConfigurations();

        boolean jettycreateflag = false;

        for (int i = 0; i < lcs.length; i++) {
            if ("com.iw.plugins.jettyrunner.PluginRunner".equals(lcs[i]
                .getAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, ""))) {

                if ("JettyWeb".equals(lcs[i].getName())) {

                    ILaunchConfigurationWorkingCopy wc =
                        lcs[i].copy("JettyRunner_" + projectName);

                    // JettyWeb�N���\���ɂĐݒ肵�Ă���v���W�F�N�g��
                    String srcProjectName =
                        wc
                            .getAttribute(
                                IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                                "");

                    // �����^�u�A�v���O�����̈���
                    String pgmArgs =
                        wc
                            .getAttribute(
                                IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                                "");

                    // �I�������v���W�F�N�g���ɂĒu��
                    wc
                        .setAttribute(
                            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                            pgmArgs.replaceAll(srcProjectName, projectName));

                    // LAUNCH_CONTEXT_ATTRIBUTE=WebAppRoot�̃t���p�X
                    String context =
                        wc.getAttribute(JettyPlugin.LAUNCH_CONTEXT_ATTRIBUTE,
                            "");

                    // �I�������v���W�F�N�g���ɂĒu��
                    wc.setAttribute(JettyPlugin.LAUNCH_CONTEXT_ATTRIBUTE,
                        context.replaceAll(srcProjectName, projectName));

                    // �v���W�F�N�g��
                    wc.setAttribute(
                        IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                        projectName);
                    wc.setAttribute(JettyPlugin.LAUNCH_CONTEXT_UI_ATTRIBUTE,
                        "src/main/webapp");
                    wc.setAttribute(JettyPlugin.LAUNCH_HOST_ATTRIBUTE,
                        "0.0.0.0");

                    ILaunchConfiguration lc = wc.doSave();
                    lc.launch(runType, null, true);

                    jettycreateflag = true;

                    break;
                }
            }
        }

        if (!jettycreateflag) {
            ConsoleUnit consoleUnit = new ConsoleUnit();
            consoleUnit.outPutStream("��O�������������A�L����JettyWeb�N���\�������݂��܂���B");
            return;
        }
    }
}