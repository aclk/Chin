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

        // Action��ID�ɂ����s�^�C�v��؂蕪����
        if ("SpiritsToolKit.JettyStarterNormal".equals(action.getId())) {
            runType = ILaunchManager.RUN_MODE;
        } else {
            runType = ILaunchManager.DEBUG_MODE;
        }

        try {
            JettyWebUtil.createLaunchForJetty(projectName, runType, project);

        } catch (Exception e) {
            // ConsoleUnit���g�p���ăR���\�[�����o�͂���
            ConsoleUnit consoleUnit = new ConsoleUnit();
            consoleUnit.outPutStream("��O�������������A�L����JettyWeb�N���\�������݂��܂���B");
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