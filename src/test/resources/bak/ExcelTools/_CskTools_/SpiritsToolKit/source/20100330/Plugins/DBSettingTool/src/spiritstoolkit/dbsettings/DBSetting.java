package spiritstoolkit.dbsettings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.core.resources.IProject;

import spiritstoolkit.dbsettings.data.HistoryData;
import spiritstoolkit.dbsettings.output.AbstractReplaceOutput;
import spiritstoolkit.dbsettings.output.ConsoleUnit;
import spiritstoolkit.dbsettings.output.ProductTestXmlReplaceOutput;
import spiritstoolkit.dbsettings.output.PropertiesReplaceOutput;
import spiritstoolkit.dbsettings.output.XmlReplaceOutput;
import spiritstoolkit.dbsettings.util.HistoryUtil;

/**
 * DB設定書き換えツールのメインクラス
 */
public class DBSetting extends JPanel implements ActionListener,
		ListSelectionListener {

	/** Properties */
	private static Properties properties;

	/** 選択したプロダクト */
	private String currentProduct;

	/** プロジェクトのルートパス */
	private String rootPath;

	/** プロジェクト選択コンボボックス */
	private JComboBox prjList;

	/** JDBC設定選択コンボボックス */
	private JComboBox jdbcList;

	/** ユーザID入力用コンポーネント */
	private JComponent uid;

	/** パスワード入力フィールド */
	private JTextField pass;

	/** SID入力フィールド */
	private JTextField sid;

	/** 履歴回数 */
	private static int historyCount = 0;

	/**
	 * Constructor
	 *
	 * @param rootPath
	 *            プロジェクトのルートパス
	 */
	public DBSetting(IProject project) {

		currentProduct = project.getName();

		try {
			loadProperties();
			rootPath = getString("prj.root");

		} catch (Exception e) {
			ConsoleUnit console = new ConsoleUnit();
			console.outPutStream(e.getMessage());
			return;
		}

		setLayout(new BorderLayout());
		JPanel labelPane = new JPanel(new GridLayout(5, 1));
		labelPane.add(new JLabel("ProjectId:"));
		labelPane.add(new JLabel("JdbcUrl:"));
		labelPane.add(new JLabel("UserId:"));
		labelPane.add(new JLabel("Password:"));
		labelPane.add(new JLabel("schema:"));
		add(labelPane, BorderLayout.WEST);

		JPanel inputPane = new JPanel(new GridLayout(5, 1));
		JPanel prjPane = new JPanel(new BorderLayout());
		java.util.List<String> list = getPrjList(currentProduct, rootPath);
		prjList = new JComboBox(list.toArray());
		prjList.setEditable(false);
		prjPane.add(prjList, BorderLayout.CENTER);
		JButton reload = new JButton("Reload");
		reload.addActionListener(this);
		reload.setActionCommand("Reload");
		prjPane.add(reload, BorderLayout.EAST);
		inputPane.add(prjPane);

		list = new ArrayList<String>();
		list.add("");
		list.addAll(getItemList("jdbc.url."));
		jdbcList = new JComboBox(list.toArray());
		jdbcList.setEditable(true);
		inputPane.add(jdbcList);

		String user = getString("defaultUser");
		list = getItemList("user.list.");
		if (!list.isEmpty()) {
			JComboBox uid = new JComboBox(list.toArray());
			uid.setEditable(true);
			uid.setSelectedItem(user);
			this.uid = uid;
		} else {
			JTextField uid = new JTextField(10);
			uid.setText(user);
			this.uid = uid;
		}
		inputPane.add(uid);
		pass = new JTextField(10);
		inputPane.add(pass);
		sid = new JTextField(10);
		inputPane.add(sid);
		add(inputPane, BorderLayout.CENTER);

		JPanel commandPane = new JPanel(new BorderLayout());

		JButton btn = new JButton("OK");
		btn.addActionListener(this);
		btn.setActionCommand("OK");

		commandPane.add(btn, BorderLayout.CENTER);

		String his = getString("history.size");
		if (!"".equals(his)) {
			historyCount = Integer.parseInt(his);
		}
		if (historyCount > 0) {
			JButton hisBtn = new JButton("History");
			hisBtn.addActionListener(this);
			hisBtn.setActionCommand("History");
			commandPane.add(hisBtn, BorderLayout.WEST);
		}

		add(commandPane, BorderLayout.SOUTH);
	}

	/**
	 * アクションイベントを処理
	 *
	 * @param e
	 *            アクションイベント
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("OK".equals(command)) {
			String prj = rootPath + "/" + prjList.getSelectedItem();
			String jdbc = jdbcList.getSelectedItem().toString();
			String user = getUser();
			String password = pass.getText();
			String schema = sid.getText();
			try {
				replace(prj, jdbc, user, password, schema);
			} catch (Exception ex) {
				ConsoleUnit console = new ConsoleUnit();
				console.outPutStream(ex.getMessage());
				return;
			}
			JOptionPane.showMessageDialog(null, "書き換え完了！("
					+ prjList.getSelectedItem() + ")");

		} else if ("Reload".equals(command)) {
			prjList.removeAllItems();
			java.util.List<String> list = getPrjList(currentProduct, rootPath);
			for (String item : list) {
				prjList.addItem(item);
			}

		} else if ("History".equals(command)) {
			// 履歴ダイアログ用のクラスを作ってもいいんだけどめんどくさかったので・・・
			Frame f = null;
			for (Container p = getParent(); p != null; p = p.getParent()) {
				if (p instanceof Frame) {
					f = (Frame) p;
				}
			}
			final JDialog dialog = new JDialog(f, "履歴から選択 (閉じる場合は×ボタンで！)", true);
			java.util.List<HistoryData> lst = HistoryUtil.getInstance(
					historyCount).loadHistory();
			JList jList = new JList(lst.toArray());
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jList.addListSelectionListener(this);
			jList.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() > 1) {
						dialog.dispose();
					}
				}
			});
			JScrollPane scPane = new JScrollPane(jList,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			dialog.getContentPane().add(scPane, BorderLayout.CENTER);
			dialog.pack();
			dialog.setSize(600, 400);
			dialog.setLocation(f.getLocation().x, f.getLocation().y
					+ f.getHeight());
			dialog.setVisible(true);
		}
	}

	/**
	 * 履歴リスト選択イベント処理
	 *
	 * @param e
	 *            ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList jList = (JList) e.getSource();
		HistoryData data = (HistoryData) jList.getSelectedValue();

		jdbcList.setSelectedItem(data.getJdbcUrl());
		if (uid instanceof JComboBox) {
			((JComboBox) uid).setSelectedItem(data.getUser());
		} else {
			((JTextField) uid).setText(data.getUser());
		}
		pass.setText(data.getPassword());
		sid.setText(data.getSchema());
	}

	/**
	 * ユーザ名を取得する。
	 *
	 * @return ユーザ名
	 */
	private String getUser() {
		if (uid instanceof JComboBox) {
			return ((JComboBox) uid).getSelectedItem().toString();
		} else {
			return ((JTextField) uid).getText();
		}
	}

	/**
	 * 指定されたキーのプレフィックスでプロパティファイルよりアイテムを一覧取得する。
	 * プロパティファイルでは[キーのプレフィックス]n（連番）で登録しておくこと
	 *
	 * @param keyPrefix
	 *            キーのプレフィックス
	 * @return アイテム一覧
	 */
	private java.util.List<String> getItemList(String keyPrefix) {
		java.util.List<String> list = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			String value = getString(keyPrefix + i);
			if ("".equals(value))
				break;
			list.add(value);
		}
		return list;
	}

	/**
	 * プロジェクト一覧を取得する
	 *
	 * @param rootPath
	 *            プロジェクトのルートパス
	 * @return プロジェクト一覧リスト
	 */
	private java.util.List<String> getPrjList(String mainProduct,
			String rootPath) {
		java.util.List<String> list = new ArrayList<String>();
		File root = new File(rootPath);
		File[] children = root.listFiles();
		boolean isEclipseWork = false;

		// 選択したプロダクトを一番上に記述
		list.add(mainProduct);
		for (File child : children) {
			// if (child.getName().startsWith("murata")) continue;
			if (isProject(child))
				list.add(child.getName());
			if (".metadata".equals(child.getName()))
				isEclipseWork = true;
		}

		// Ecripseとの同期処理（あかんなぁ・・・・）
		// Ecripseの設定用フォルダにスナップファイル（.syncinfo.snap）があるかどうかで開いているプロジェクトを判定している
		if (isEclipseWork && Boolean.parseBoolean(getString("sync.eclipse"))) {
			java.util.List<String> tmpList = new ArrayList<String>();
			tmpList.add(mainProduct);
			File metaRoot = new File(root.getAbsolutePath() + File.separator
					+ ".metadata/.plugins/org.eclipse.core.resources/.projects");
			File[] prjFiles = metaRoot.listFiles();
			for (File prjFile : prjFiles) {
				if (prjFile.isFile())
					continue;
				File snap = new File(prjFile.getAbsolutePath() + File.separator
						+ ".syncinfo.snap");
				if (snap.exists() && list.contains(prjFile.getName())) {
					tmpList.add(prjFile.getName());
				}
			}
			if (tmpList.size() > 0)
				return tmpList;
		}
		return list;
	}

	/**
	 * 指定されたパスがプロジェクトかどうか判定する。 指定したパスにpom.xmlがあるかどうかで判定している。
	 *
	 * @param prjFile
	 *            プロジェクトのパス
	 * @return 判定結果（プロジェクトならTrue）
	 */
	private static boolean isProject(File prjFile) {
		if (prjFile.isFile())
			return false;
		File pom = new File(prjFile.getAbsolutePath() + File.separator
				+ "pom.xml");
		return pom.exists();
	}

	/** 書き換えするやつら */
	private static final AbstractReplaceOutput[] REPLACERS = {
			new PropertiesReplaceOutput(), new XmlReplaceOutput(),
			new ProductTestXmlReplaceOutput(), };

	/**
	 * DB設定を書き換える。
	 *
	 * @param prjRootPath
	 *            プロジェクトのパス
	 * @param jdbcUrl
	 *            JDBC接続文字列
	 * @param user
	 *            userId
	 * @param password
	 *            password
	 * @param schema
	 *            SID
	 */
	private static void replace(String prjRootPath, String jdbcUrl,
			String user, String password, String schema) throws Exception {
		for (AbstractReplaceOutput rep : REPLACERS) {
			rep.replaceOutput(prjRootPath, jdbcUrl, user, password, schema);
		}
		if (historyCount > 0) {
			HistoryUtil.getInstance(historyCount).addHistory(jdbcUrl, user,
					password, schema);
		}
	}

	/**
	 * プロパティファイルより指定されたキーの設定値を文字列で取得する。
	 *
	 * @param key
	 *            プロパティファイルのキー文字列
	 * @return 設定値
	 */
	private static String getString(String key) {
		try {
			String value = properties.getProperty(key);
			return value;
		} catch (Exception e) {
			ConsoleUnit console = new ConsoleUnit();
			console.outPutStream(e.getMessage());
		}
		return "";
	}

	private static void loadProperties() throws FileNotFoundException,
			IOException {

		Properties pp = new Properties();
		FileInputStream fis = new FileInputStream("C:/dbSetting.properties");
		pp.load(fis);

		properties = pp;
	}
}
