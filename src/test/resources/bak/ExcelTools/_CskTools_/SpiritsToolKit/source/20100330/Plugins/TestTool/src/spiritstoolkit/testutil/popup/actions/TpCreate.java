package spiritstoolkit.testutil.popup.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import spiritstoolkit.testutil.output.ConsoleUnit;
import spiritstoolkit.testutil.util.TestManagerUtil;

public class TpCreate implements IObjectActionDelegate {

    private IFolder iFolder;

    private List<IFile> iFileList;

    /**
     * Constructor for Action1.
     */
    public TpCreate() {
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

        if (iFolder == null && iFileList == null) {
            return;
        }

        // 利用者が必ずワークスペースを使っている訳ではないので
        //        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        ConsoleUnit consoleUnit = new ConsoleUnit();

        consoleUnit.outPutStream("TP create [ TR -> TP ]:Running...");

        if (iFolder != null) {

            if ("regression".equals(iFolder.getName())) {

                // 選択先に格納されているファイル
                IPath srcPath = iFolder.getLocation();

                IPath descPath =
                    iFolder.getLocation().removeLastSegments(1).append("input");

                File filses = new File(srcPath.toString());

                File[] fileList = filses.listFiles();

                for (int i = 0; i < fileList.length; i++) {
                    TestManagerUtil.createTpFile(fileList[i], descPath,
                        consoleUnit);
                }

            }

        } else if (iFileList != null) {

            IFile iFile;

            for (int i = 0; i < iFileList.size(); i++) {

                if (iFileList.get(i) instanceof IFile) {
                    iFile = iFileList.get(i);

                    String folderPath =
                        iFile.getFullPath().removeLastSegments(1).toString();

                    if (folderPath.endsWith("regression")) {

                        // 選択先に格納されているファイル
                        IPath descPath =
                            iFile.getLocation().removeLastSegments(2).append(
                                "input");

                        File file = new File(iFile.getLocation().toString());

                        TestManagerUtil.createTpFile(file, descPath,
                            consoleUnit);
                    }
                }
            }
        }

        consoleUnit.outPutStream("TP create [ TR -> TP ]:End...");

    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        iFolder = null;
        iFileList = null;

        if (selection instanceof StructuredSelection) {
            StructuredSelection structuredSelection =
                (StructuredSelection)selection;

            if (structuredSelection.size() > 1) {
                iFileList = structuredSelection.toList();
            } else {
                if (structuredSelection.getFirstElement() instanceof IFolder) {
                    iFolder = (IFolder)structuredSelection.getFirstElement();
                } else if (structuredSelection.getFirstElement() instanceof IFile) {
                    iFileList = new ArrayList<IFile>();
                    iFileList.add((IFile)structuredSelection.getFirstElement());
                }
            }
        }
    }
}
