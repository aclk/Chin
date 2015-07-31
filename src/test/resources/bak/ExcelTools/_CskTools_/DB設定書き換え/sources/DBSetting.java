import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

import output.*;
import util.*;
import data.*;

/**
 * DB設定書き換えツールのメインクラス
 */
public class DBSetting extends JPanel implements ActionListener, ListSelectionListener {
	/** ResourceBundle */
	private static ResourceBundle bundle;
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
	 * @param rootPath プロジェクトのルートパス
	 */
	public DBSetting(String rootPath) {
		this.rootPath = rootPath;
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
		java.util.List<String> list = getPrjList(rootPath);
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
		if (!"".equals(his)){
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
	 * @param e アクションイベント
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
				JOptionPane.showMessageDialog(this, ex.getMessage());
				return;
			}
			JOptionPane.showMessageDialog(this, "書き換え完了！(" + prjList.getSelectedItem() + ")");
		} else if ("Reload".equals(command)) {
			prjList.removeAllItems();
			java.util.List<String> list = getPrjList(rootPath);
			for (String item : list) {
				prjList.addItem(item);
			}
		} else if("History".equals(command)) {
			// 履歴ダイアログ用のクラスを作ってもいいんだけどめんどくさかったので・・・
			Frame f = null;
			for (Container p = getParent(); p != null; p = p.getParent()) {
				if (p instanceof Frame) {
					f = (Frame)p;
				}
			}
			final JDialog dialog = new JDialog(f, "履歴から選択 (閉じる場合は×ボタンで！)", true);
			java.util.List<HistoryData> lst = HistoryUtil.getInstance(historyCount).loadHistory();
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
			dialog.setLocation(f.getLocation().x, f.getLocation().y + f.getHeight());
			dialog.setVisible(true);
		}
	}
	
	/**
	 * 履歴リスト選択イベント処理
	 *
	 * @param e ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList jList = (JList)e.getSource();
		HistoryData data = (HistoryData)jList.getSelectedValue();
		
		jdbcList.setSelectedItem(data.getJdbcUrl());
		if (uid instanceof JComboBox) {
			((JComboBox)uid).setSelectedItem(data.getUser());
		} else {
			((JTextField)uid).setText(data.getUser());
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
			return ((JComboBox)uid).getSelectedItem().toString();
		} else {
			return ((JTextField)uid).getText();
		}
	}
	
	/**
	 * 指定されたキーのプレフィックスでプロパティファイルよりアイテムを一覧取得する。
	 * プロパティファイルでは[キーのプレフィックス]n（連番）で登録しておくこと
	 *
	 * @param keyPrefix キーのプレフィックス
	 * @return アイテム一覧
	 */
	private java.util.List<String> getItemList(String keyPrefix) {
		java.util.List<String> list =new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			String value = getString(keyPrefix + i);
			if ("".equals(value)) break;
			list.add(value);
		}
		return list;
	}
	
	/**
	 * プロジェクト一覧を取得する
	 *
	 * @param rootPath プロジェクトのルートパス
	 * @return プロジェクト一覧リスト
	 */
	private java.util.List<String> getPrjList(String rootPath) {
		java.util.List<String> list = new ArrayList<String>();
		File root = new File(rootPath);
		File[] children = root.listFiles();
		boolean isEclipseWork = false;
		for (File child : children) {
			// if (child.getName().startsWith("murata")) continue;
			if (isProject(child)) list.add(child.getName());
			if (".metadata".equals(child.getName())) isEclipseWork = true;
		}
		
		// Ecripseとの同期処理（あかんなぁ・・・・）
		// Ecripseの設定用フォルダにスナップファイル（.syncinfo.snap）があるかどうかで開いているプロジェクトを判定している
		if(isEclipseWork && Boolean.parseBoolean(getString("sync.eclipse"))) {
			java.util.List<String> tmpList = new ArrayList<String>();
			File metaRoot = new File(root.getAbsolutePath() + File.separator
			    + ".metadata/.plugins/org.eclipse.core.resources/.projects");
			File[] prjFiles = metaRoot.listFiles();
			for (File prjFile :  prjFiles) {
				if (prjFile.isFile()) continue;
				File snap = new File(prjFile.getAbsolutePath() + File.separator
				    + ".syncinfo.snap");
				if (snap.exists() && list.contains(prjFile.getName())) {
					tmpList.add(prjFile.getName());
				}
			}
			if (tmpList.size() > 0) return tmpList;
		}
		return list;
	}
	
	/**
	 * 指定されたパスがプロジェクトかどうか判定する。
	 * 指定したパスにpom.xmlがあるかどうかで判定している。
	 *
	 * @param prjFile プロジェクトのパス
	 * @return 判定結果（プロジェクトならTrue）
	 */
	private static boolean isProject(File prjFile) {
		if (prjFile.isFile()) return false;
		File pom = new File(prjFile.getAbsolutePath() + File.separator + "pom.xml");
		return pom.exists();
	}
	
	/** 書き換えするやつら */
	private static final AbstractReplaceOutput[] REPLACERS = {
		new PropertiesReplaceOutput(),
		new XmlReplaceOutput(),
		new ProductTestXmlReplaceOutput(),
	};
	
	/**
	 * DB設定を書き換える。
	 *
	 * @param prjRootPath プロジェクトのパス
	 * @param jdbcUrl JDBC接続文字列
	 * @param user userId
	 * @param password password
	 * @param schema SID
	 */
	private static void replace(String prjRootPath,
                                String jdbcUrl,
                                String user,
                                String password,
                                String schema) throws Exception {
        for (AbstractReplaceOutput rep : REPLACERS) {
        	rep.replaceOutput(prjRootPath, jdbcUrl, user, password, schema);
        }
		if (historyCount > 0) {
			HistoryUtil.getInstance(historyCount).addHistory(jdbcUrl, user, password, schema);
		}
	}
	
	/**
	 * プロパティファイルより指定されたキーの設定値を文字列で取得する。
	 *
	 * @param key プロパティファイルのキー文字列
	 * @return 設定値
	 */
	private static String getString(String key) {
		try {
			String value = bundle.getString(key);
			return value;
		} catch (Exception e) {}
		return "";
	}
	
	/**
	 * メイン処理
	 *
	 * @param args 起動引数
	 */
	public static void main(String[] args) {
		String rootPath = null;
		try {
			bundle = ResourceBundle.getBundle(System.getProperty("db.config","dbSetting"));
			rootPath = getString("prj.root");
			if ("".equals(rootPath)) rootPath = ".";
		} catch (Exception e) {
			if (args.length == 0) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			e.printStackTrace();
			System.exit(-1);
		}
		if (args.length == 0) {
			JFrame f = new JFrame();
			f.setTitle("DB設定書き換え (Ver0.0.14)");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			f.getContentPane().add(new DBSetting(rootPath));
			f.pack();
			
			f.setSize(400, f.getHeight());
			f.setVisible(true);
		} else {
			String prjId = args[0];
			if (prjId.equals("help")) {
				System.out.println("DB設定書き換えツール");
				System.out.println("引数なしで起動するとGUIで操作できます。");
				System.out.println("コマンドラインで使用する場合は次のとおり");
				System.out.println("Usage: java -jar DBSetting.jar ProjectId [JdbcUrlNo] [UserId] [Password] [Schema]");
				System.out.println("\t- JdbcUrlNoはdbSetting.properties接続リストの連番\n\t   (無い番号を指定するとJdbcUrlは書き換えません)");
				System.out.println("\t- UserIdが未指定の場合はデフォルトユーザIDを使用します。");
				System.exit(0);
				return;
			}
			
			File prjDir = new File(rootPath + File.separator + prjId);
			if (!isProject(prjDir)) {
				System.out.println("Projectがない");
				System.exit(-1);
			}
			String prj = prjDir.getAbsolutePath();
			String jdbc = "";
			String user = getString("defaultUser");
			String password = "";
			String schema = "";
			try {
				jdbc = getString("jdbc.url."+ args[1]);
				user = args[2];
				password = args[3];
				schema = args[4];
				
			} catch (Exception e){}
			try {
				replace(prj, jdbc, user, password, schema);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
				return;
			}
			System.exit(0);
		}
	}
}
