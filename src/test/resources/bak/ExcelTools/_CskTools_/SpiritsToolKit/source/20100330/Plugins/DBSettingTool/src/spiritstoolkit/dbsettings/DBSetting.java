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
 * DB�ݒ菑�������c�[���̃��C���N���X
 */
public class DBSetting extends JPanel implements ActionListener,
		ListSelectionListener {

	/** Properties */
	private static Properties properties;

	/** �I�������v���_�N�g */
	private String currentProduct;

	/** �v���W�F�N�g�̃��[�g�p�X */
	private String rootPath;

	/** �v���W�F�N�g�I���R���{�{�b�N�X */
	private JComboBox prjList;

	/** JDBC�ݒ�I���R���{�{�b�N�X */
	private JComboBox jdbcList;

	/** ���[�UID���͗p�R���|�[�l���g */
	private JComponent uid;

	/** �p�X���[�h���̓t�B�[���h */
	private JTextField pass;

	/** SID���̓t�B�[���h */
	private JTextField sid;

	/** ������ */
	private static int historyCount = 0;

	/**
	 * Constructor
	 *
	 * @param rootPath
	 *            �v���W�F�N�g�̃��[�g�p�X
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
	 * �A�N�V�����C�x���g������
	 *
	 * @param e
	 *            �A�N�V�����C�x���g
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
			JOptionPane.showMessageDialog(null, "�������������I("
					+ prjList.getSelectedItem() + ")");

		} else if ("Reload".equals(command)) {
			prjList.removeAllItems();
			java.util.List<String> list = getPrjList(currentProduct, rootPath);
			for (String item : list) {
				prjList.addItem(item);
			}

		} else if ("History".equals(command)) {
			// �����_�C�A���O�p�̃N���X������Ă������񂾂��ǂ߂�ǂ����������̂ŁE�E�E
			Frame f = null;
			for (Container p = getParent(); p != null; p = p.getParent()) {
				if (p instanceof Frame) {
					f = (Frame) p;
				}
			}
			final JDialog dialog = new JDialog(f, "��������I�� (����ꍇ�́~�{�^���ŁI)", true);
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
	 * �������X�g�I���C�x���g����
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
	 * ���[�U�����擾����B
	 *
	 * @return ���[�U��
	 */
	private String getUser() {
		if (uid instanceof JComboBox) {
			return ((JComboBox) uid).getSelectedItem().toString();
		} else {
			return ((JTextField) uid).getText();
		}
	}

	/**
	 * �w�肳�ꂽ�L�[�̃v���t�B�b�N�X�Ńv���p�e�B�t�@�C�����A�C�e�����ꗗ�擾����B
	 * �v���p�e�B�t�@�C���ł�[�L�[�̃v���t�B�b�N�X]n�i�A�ԁj�œo�^���Ă�������
	 *
	 * @param keyPrefix
	 *            �L�[�̃v���t�B�b�N�X
	 * @return �A�C�e���ꗗ
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
	 * �v���W�F�N�g�ꗗ���擾����
	 *
	 * @param rootPath
	 *            �v���W�F�N�g�̃��[�g�p�X
	 * @return �v���W�F�N�g�ꗗ���X�g
	 */
	private java.util.List<String> getPrjList(String mainProduct,
			String rootPath) {
		java.util.List<String> list = new ArrayList<String>();
		File root = new File(rootPath);
		File[] children = root.listFiles();
		boolean isEclipseWork = false;

		// �I�������v���_�N�g����ԏ�ɋL�q
		list.add(mainProduct);
		for (File child : children) {
			// if (child.getName().startsWith("murata")) continue;
			if (isProject(child))
				list.add(child.getName());
			if (".metadata".equals(child.getName()))
				isEclipseWork = true;
		}

		// Ecripse�Ƃ̓��������i������Ȃ��E�E�E�E�j
		// Ecripse�̐ݒ�p�t�H���_�ɃX�i�b�v�t�@�C���i.syncinfo.snap�j�����邩�ǂ����ŊJ���Ă���v���W�F�N�g�𔻒肵�Ă���
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
	 * �w�肳�ꂽ�p�X���v���W�F�N�g���ǂ������肷��B �w�肵���p�X��pom.xml�����邩�ǂ����Ŕ��肵�Ă���B
	 *
	 * @param prjFile
	 *            �v���W�F�N�g�̃p�X
	 * @return ���茋�ʁi�v���W�F�N�g�Ȃ�True�j
	 */
	private static boolean isProject(File prjFile) {
		if (prjFile.isFile())
			return false;
		File pom = new File(prjFile.getAbsolutePath() + File.separator
				+ "pom.xml");
		return pom.exists();
	}

	/** �������������� */
	private static final AbstractReplaceOutput[] REPLACERS = {
			new PropertiesReplaceOutput(), new XmlReplaceOutput(),
			new ProductTestXmlReplaceOutput(), };

	/**
	 * DB�ݒ������������B
	 *
	 * @param prjRootPath
	 *            �v���W�F�N�g�̃p�X
	 * @param jdbcUrl
	 *            JDBC�ڑ�������
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
	 * �v���p�e�B�t�@�C�����w�肳�ꂽ�L�[�̐ݒ�l�𕶎���Ŏ擾����B
	 *
	 * @param key
	 *            �v���p�e�B�t�@�C���̃L�[������
	 * @return �ݒ�l
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
