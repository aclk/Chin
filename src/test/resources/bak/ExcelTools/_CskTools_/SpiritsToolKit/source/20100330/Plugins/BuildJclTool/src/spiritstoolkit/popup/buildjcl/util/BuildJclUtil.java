package spiritstoolkit.popup.buildjcl.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class BuildJclUtil {

	public static void createLaunchForBuildJcl(String projectName,
			String runType) throws CoreException {

		// �����`�E�}�l�[�W���̎擾
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();

		ILaunchConfiguration[] lcs = manager.getLaunchConfigurations();

		boolean existsFlag = false;

		for (int i = 0; i < lcs.length; i++) {
			if ((projectName + "_buildjcl").equals(lcs[i].getName())) {
				lcs[i].launch(runType, null);
				existsFlag = true;
				break;
			}
		}

		if (!existsFlag) {
			// �����`�E�}�l�[�W���̎擾
			ILaunchConfigurationType type = manager
					.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
			ILaunchConfigurationWorkingCopy wc = type.newInstance(null,
					projectName + "_buildjcl");

			// �v���W�F�N�g���̐ݒ�
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					projectName);
			// ���C���E�N���X���̐ݒ�
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					"org.apache.tools.ant.launch.Launcher");
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
					"-buildfile build-jcl.xml");
			wc.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
					"-Xmx512m");
			ILaunchConfiguration lc = wc.doSave();
			lc.launch(runType, null);
		}
	}
}