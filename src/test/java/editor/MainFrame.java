package editor;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
	EditPanel editPanel;

	MainFrame() {
		getContentPane().setLayout(new BorderLayout());

		/* 見た目をWindows Likeに */
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/* メニューバーの追加 */
		setJMenuBar(getDefaultMenuBar());

		/* ツールバーの追加 */
		getContentPane().add(getDefaultToolBar(), BorderLayout.NORTH);

		/* ステータスバーの追加 */
		getContentPane().add(getDefaultStatusBar(), BorderLayout.SOUTH);

		/* 編集部分のパネル追加 */
		this.editPanel = new EditPanel();
		getContentPane().add(getDefaultEditPanel(), BorderLayout.CENTER);

		/* JFrameの設定 */
		setTitle("タイトル");
		setSize(700, 500);
		setLocation(10, 10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		initComponent();
	}

	public void initComponent() {
		this.editPanel.testMethod();
		this.editPanel.setLinePane();
		this.editPanel.setHeadPane();
	}

	private JMenuBar getDefaultMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// ファイル(F)
		JMenu menuFile = new JMenu("ファイル(F)");
		menuFile.setMnemonic('F');
		{
			JMenuItem menuFile_new = new JMenuItem("新規作成(N)", 'N');
			menuFile_new.setAccelerator(KeyStroke.getKeyStroke('N',
					KeyEvent.CTRL_MASK));
			menuFile_new.setIcon(new ImageIcon("new.png"));
			menuFile.add(menuFile_new);

			menuFile.addSeparator();

			JMenuItem menuFile_exit = new JMenuItem("閉じる(X)", 'X');
			menuFile.add(menuFile_exit);
		}
		menuBar.add(menuFile);

		// 編集(E)
		JMenu menuEdit = new JMenu("編集(E)");
		menuEdit.setMnemonic('E');
		{
			JMenuItem menuEdit_undo = new JMenuItem("元に戻す(U)", 'U');
			menuEdit_undo.setAccelerator(KeyStroke.getKeyStroke('Z',
					KeyEvent.CTRL_MASK));
			menuEdit_undo.setIcon(new ImageIcon(
					"src/test/resources/editor/undo.png"));
			menuEdit.add(menuEdit_undo);

			JMenuItem menuEdit_redo = new JMenuItem("やり直し(R)", 'R');
			menuEdit_redo.setAccelerator(KeyStroke.getKeyStroke('Y',
					KeyEvent.CTRL_MASK));
			menuEdit_redo.setIcon(new ImageIcon(
					"src/test/resources/editor/redo.png"));
			menuEdit.add(menuEdit_redo);

			menuEdit.addSeparator();

			JMenuItem menuEdit_cut = new JMenuItem("切り取り(T)", 'T');
			menuEdit_cut.setAccelerator(KeyStroke.getKeyStroke('X',
					KeyEvent.CTRL_MASK));
			menuEdit_cut.setIcon(new ImageIcon(
					"src/test/resources/editor/cut.png"));
			menuEdit.add(menuEdit_cut);

			JMenuItem menuEdit_copy = new JMenuItem("コピー(C)", 'T');
			menuEdit_copy.setAccelerator(KeyStroke.getKeyStroke('C',
					KeyEvent.CTRL_MASK));
			menuEdit_copy.setIcon(new ImageIcon(
					"src/test/resources/editor/copy.png"));
			menuEdit.add(menuEdit_copy);

			JMenuItem menuEdit_yunk = new JMenuItem("貼り付け(Y)", 'T');
			menuEdit_yunk.setAccelerator(KeyStroke.getKeyStroke('Y',
					KeyEvent.CTRL_MASK));
			menuEdit_yunk.setIcon(new ImageIcon(
					"src/test/resources/editor/yunk.png"));
			menuEdit.add(menuEdit_yunk);

			menuEdit.addSeparator();

			JMenuItem menuEdit_find = new JMenuItem("検索(F)", 'F');
			menuEdit_find.setAccelerator(KeyStroke.getKeyStroke('F',
					KeyEvent.CTRL_MASK));
			menuEdit_find.setIcon(new ImageIcon(
					"src/test/resources/editor/find.png"));
			menuEdit.add(menuEdit_find);
		}
		menuBar.add(menuEdit);

		// ヘルプ(H)
		JMenu menuHelp = new JMenu("ヘルプ(H)");
		menuFile.setMnemonic('H');
		{
			JMenuItem menuHelp_ver = new JMenuItem("バージョン(A)", 'A');
			menuHelp.add(menuHelp_ver);
		}
		menuBar.add(menuHelp);

		return menuBar;
	}

	private JToolBar getDefaultToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		JButton btn_new = new JButton(new ImageIcon(
				"src/test/resources/editor/new.png"));
		btn_new.setToolTipText("新規作成");
		toolBar.add(btn_new);

		toolBar.addSeparator();

		JButton btn_cut = new JButton(new ImageIcon(
				"src/test/resources/editor/cut.png"));
		btn_cut.setToolTipText("カット");
		toolBar.add(btn_cut);

		JButton btn_copy = new JButton(new ImageIcon(
				"src/test/resources/editor/copy.png"));
		btn_copy.setToolTipText("コピー");
		toolBar.add(btn_copy);

		JButton btn_yunk = new JButton(new ImageIcon(
				"src/test/resources/editor/yunk.png"));
		btn_yunk.setToolTipText("貼り付け");
		toolBar.add(btn_yunk);

		toolBar.addSeparator();

		JButton btn_undo = new JButton(new ImageIcon(
				"src/test/resources/editor/undo.png"));
		btn_undo.setToolTipText("元に戻す");
		toolBar.add(btn_undo);

		JButton btn_redo = new JButton(new ImageIcon(
				"src/test/resources/editor/redo.png"));
		btn_redo.setToolTipText("やり直し");
		toolBar.add(btn_redo);

		return toolBar;
	}

	private JComponent getDefaultStatusBar() {
		return new JLabel("ステータス");
	}

	private JComponent getDefaultEditPanel() {
		return this.editPanel;
	}
}