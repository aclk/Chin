package jp.mk.rmsresourcepursuer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.logging.LogFactory;

/**
 * データチェッカーのUI。
 * @author K.Miura
 *
 */
public class ImportForm extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar jJMenuBar = null;
	private JPanel base = null;
	private JPanel settingBase = null;
	private JPanel buttonBase = null;
	private JButton execute = null;
	private JButton cansel = null;
	private JLabel lblOutputSetting = null;
	private JLabel lblOutputDirectory = null;
	private JTextField txtOutputDirectory = null;
	private JLabel jLabel = null;
	private JLabel lblOutputCaption = null;
	private JTextField txtOutputCaption = null;
	private JLabel lblQuerySetting = null;
	private JLabel lbllblOutputDirectory2 = null;
	private JLabel lblSearchQuery = null;
	private JTextArea txtSearchQuery = null;

	private SqlExecuter excuter = null;

	/**
	 * This method initializes base
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getBase() {
		if (base == null) {
			base = new JPanel();
			base.setLayout(new BorderLayout());
			base.add(getSettingBase(), java.awt.BorderLayout.CENTER);
			base.add(getButtonBase(), java.awt.BorderLayout.SOUTH);
		}
		return base;
	}

	/**
	 * This method initializes settingBase
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getSettingBase() {
		if (settingBase == null) {
			GridLayout gridLayout2 = new GridLayout();
			gridLayout2.setRows(10);
			gridLayout2.setColumns(2);
			lblSearchQuery = new JLabel();
			lblSearchQuery.setText("条件SQL");
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(10);
			gridLayout1.setColumns(2);
			lbllblOutputDirectory2 = new JLabel();
			lbllblOutputDirectory2.setText("");
			lblQuerySetting = new JLabel();
			lblQuerySetting.setText("抽出条件設定");
			lblOutputCaption = new JLabel();
			lblOutputCaption.setText("お題目");
			jLabel = new JLabel();
			jLabel.setText("");
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(10);
			gridLayout.setColumns(2);
			lblOutputSetting = new JLabel();
			lblOutputSetting.setText("出力先設定");
			lblOutputDirectory = new JLabel();
			lblOutputDirectory.setText("出力ディレクトリ");
			lblOutputDirectory.setPreferredSize(new java.awt.Dimension(40,16));
			settingBase = new JPanel();
			settingBase.setLayout(gridLayout2);
			settingBase.add(lblOutputSetting, null);
			settingBase.add(jLabel, null);
			settingBase.add(lblOutputDirectory, null);
			settingBase.add(getTxtDriverClass(), null);
			settingBase.add(lblOutputCaption, null);
			settingBase.add(getTxtOutputCaption(), null);
			settingBase.add(lblQuerySetting, null);
			settingBase.add(lbllblOutputDirectory2, null);
			settingBase.add(lblSearchQuery, null);
			settingBase.add(getTxtSearchQuery(), null);
		}
		return settingBase;
	}

	/**
	 * This method initializes buttonBase
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonBase() {
		if (buttonBase == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			buttonBase = new JPanel();
			buttonBase.setLayout(flowLayout);
			buttonBase.add(getExecute(), null);
			buttonBase.add(getCansel(), null);
		}
		return buttonBase;
	}

	/**
	 * This method initializes execute
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getExecute() {
		if (execute == null) {
			execute = new JButton();
			execute.setText("実行");
			execute.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()");
					// データチェックの実行

					excuter.setDriverClass("oracle.jdbc.driver.OracleDriver");
					excuter.setJdbcUrl("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = m0tudb10)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = m0tudb20)(PORT = 1521))(LOAD_BALANCE = yes))(CONNECT_DATA =(SERVICE_NAME = m0tudb.murata.co.jp)))");
					excuter.setUser("SCHM_C001");
					excuter.setPassword("SCHM_C001");

					excuter.setOutputDirectory(txtOutputDirectory.getText());
					excuter.setCaption(txtOutputCaption.getText());
//					excuter.setSearchQuery(txtSearchQuery.getText());


					if (excuter.execute()) {
						JOptionPane.showMessageDialog((Component) e.getSource() , "データチェックが成功しました。", "データチェックツール" , JOptionPane.INFORMATION_MESSAGE );
					} else {
						JOptionPane.showMessageDialog((Component) e.getSource() , "データチェックが失敗しました。", "データチェックツール" , JOptionPane.ERROR_MESSAGE );

					}

				}
			});
		}
		return execute;
	}

	/**
	 * This method initializes cansel
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getCansel() {
		if (cansel == null) {
			cansel = new JButton();
			cansel.setText("キャンセル");
			cansel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()");
					System.exit(0);
				}
			});
		}
		return cansel;
	}

	/**
	 * This method initializes txtDriverClass
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtDriverClass() {
		if (txtOutputDirectory == null) {
			txtOutputDirectory = new JTextField();
		}
		return txtOutputDirectory;
	}

	/**
	 * This method initializes txtOutputCaption
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtOutputCaption() {
		if (txtOutputCaption == null) {
			txtOutputCaption = new JTextField();
		}
		return txtOutputCaption;
	}



	/**
	 * This method initializes txtSearchQuery
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextArea getTxtSearchQuery() {
		if (txtSearchQuery == null) {
			txtSearchQuery = new JTextArea();
			txtSearchQuery.setSize(100, 100);
		}
		return txtSearchQuery;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImportForm application = new ImportForm();
		application.setVisible(true);
	}

	/**
	 * This is the default constructor
	 */
	public ImportForm() {
		super();
		initialize();
//		excuter = new SqlExecuter();
		excuter.setLog( LogFactory.getLog("file"));
//		this.setTitle(this.getTitle() + " - " + excuter.getDescription());

	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getBase());
		this.setJMenuBar(getJJMenuBar());
		this.setSize(450, 266);
		this.setTitle("データチェックツール");
	}

	/**
	 * This method initializes jJMenuBar
	 *
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
		}
		return jJMenuBar;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
