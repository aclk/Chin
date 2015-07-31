package spiritstoolkit.popup.actions;

import javax.swing.JFrame;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import spiritstoolkit.dbsettings.DBSetting;
import spiritstoolkit.dbsettings.output.ConsoleUnit;

public class DBSettingTool implements IObjectActionDelegate {

	private IProject project;

	/**
	 * Constructor for Action1.
	 */
	public DBSettingTool() {
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
		try {

			// DBê›íËèëÇ´ä∑Ç¶ÉcÅ[ÉããNìÆ
			JFrame f = new JFrame();
			f.setTitle("DBê›íËèëÇ´ä∑Ç¶ (For eclipse)");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.getContentPane().add(new DBSetting(project));
			f.pack();
			f.setSize(400, f.getHeight());
			f.setVisible(true);

		} catch (Exception e) {
			ConsoleUnit console = new ConsoleUnit();
			console.outPutStream(e.getMessage());
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
