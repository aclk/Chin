package jp.mk.formater;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 * SqlSharp結果の列並替ツールのUI。
 * @author K.Miura
 *
 */
public class Formater extends JFrame {

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
	private JTextArea resultString = null;
	private JTextArea columString = null;
	private JTextArea dataString = null;


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

			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			gridLayout.setColumns(1);
			gridLayout.setVgap(5);

			columString = new JTextArea();
			columString.setText("");

			dataString = new JTextArea();
			dataString.setText("");

			resultString = new JTextArea();
			resultString.setText("");

			settingBase = new JPanel();

			settingBase.setLayout(gridLayout);

			settingBase.add(columString, null);
			settingBase.add(dataString, null);
			settingBase.add(resultString, null);

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
					// フォーマットの実行

					TextConverter formater = new TextConverter();

					formater.setColumn(columString.getText());
					formater.setDataText(dataString.getText());

					resultString.setText(formater.format());

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
	 * @param args
	 */
	public static void main(String[] args) {
		Formater application = new Formater();
		application.setVisible(true);
	}

	/**
	 * This is the default constructor
	 */
	public Formater() {
		super();
		initialize();
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
		this.setTitle("SQL#結果フォーマット補助ツール");
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
