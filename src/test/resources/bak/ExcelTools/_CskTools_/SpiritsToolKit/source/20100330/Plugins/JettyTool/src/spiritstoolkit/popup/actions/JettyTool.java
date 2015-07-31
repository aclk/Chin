package spiritstoolkit.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import spiritstoolkit.popup.jetty.output.ConsoleUnit;
import spiritstoolkit.popup.jetty.util.JettyWebUtil;

public class JettyTool implements IObjectActionDelegate {

    private IProject project;

    /**
     * Constructor for Action1.
     */
    public JettyTool() {
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
        if ("SpiritsToolKit.JettyStarterNormal".equals(action.getId())) {
            runType = ILaunchManager.RUN_MODE;
        } else {
            runType = ILaunchManager.DEBUG_MODE;
        }

        try {
            JettyWebUtil.createLaunchForJetty(projectName, runType, project);

        } catch (Exception e) {
            // ConsoleUnitを使用してコンソールを出力する
            ConsoleUnit consoleUnit = new ConsoleUnit();
            consoleUnit.outPutStream("例外が発生したか、有効なJettyWeb起動構成が存在しません。");
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        StructuredSelection ss = (StructuredSelection)selection;
        Object obj = ss.getFirstElement();
        if (obj instanceof IProject) {
            project = (IProject)obj;
        }
    }
}