package spiritstoolkit.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import spiritstoolkit.popup.buildjcl.util.BuildJclUtil;

public class BuildJclTool implements IObjectActionDelegate {

	private IProject project;

	/**
	 * Constructor for Action1.
	 */
	public BuildJclTool() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		String projectName = project.getName();
		String runType = "";

		// ActionのIDにより実行タイプを切り分ける
		if ("SpiritsToolKit.BuildJclStarterNormal".equals(action.getId())) {
			runType = ILaunchManager.RUN_MODE;
		} else {
			runType = ILaunchManager.DEBUG_MODE;
		}
		try {
			BuildJclUtil.createLaunchForBuildJcl(projectName, runType);
		} catch (Exception e) {

		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		StructuredSelection ss = (StructuredSelection) selection;
		Object obj = ss.getFirstElement();
		if (obj instanceof IProject) {
			project = (IProject) obj;
		}
	}
}
