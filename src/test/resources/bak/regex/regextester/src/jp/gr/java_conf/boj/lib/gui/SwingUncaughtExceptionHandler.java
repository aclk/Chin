package jp.gr.java_conf.boj.lib.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Swingアプリケーションにおいてキャッチされなかった例外を
 * jp.gr.java_conf.boj.lib.gui.MessageDialog を使って表示する内容で
 * Thread.UncaughtExceptionHandler インターフェイスを実装。
 *
 */
public final class SwingUncaughtExceptionHandler 
						implements Thread.UncaughtExceptionHandler{

	private final MessageDialog.DialogInfo dialogInfo;
	private final Dialog ownerDialog;
	private final Frame ownerFrame;
	
	/**
	 * 閉じるボタンと例外のスタックトレースを表示した
	 * テキストエリアで構成されるモーダルダイアログを表示する。<br/>
	 * ownerは表示するモーダルダイアログの親フレーム。
	 * 
	 * @param owner 親フレーム。nullでもよい。
	 */
	public SwingUncaughtExceptionHandler(Frame owner) {
		this(owner, null, null);
	}
	
	/**
	 * 閉じるボタンと例外のスタックトレースを表示した
	 * テキストエリアで構成されるモーダルダイアログを表示する。<br/>
	 * ownerは表示するモーダルダイアログの親ダイアログ。
	 * 
	 * @param owner 親ダイアログ。nullでもよい。
	 */
	public SwingUncaughtExceptionHandler(Dialog owner) {
		this(owner, null, null);
	}
	
	/**
	 * 閉じるボタンと例外のスタックトレースを表示した
	 * テキストエリアとユーザへのメッセージで構成される
	 * モーダルダイアログを表示する。<br/>
	 * 最上部にメッセージが表示され、その下にテキストエリア、
	 * 最下部にボタンが配置される。<br/>
	 * メッセージに改行(\n \r \r\n)が含まれる場合には
	 * 改行して表示される。<br/>
	 * message が null の場合には何も表示されない。<br/>
	 * ownerは表示するモーダルダイアログの親フレーム。
	 * 
	 * @param owner 親フレーム。nullでもよい。
	 * @param message ダイアログに表示するメッセージ
	 */
	public SwingUncaughtExceptionHandler(Frame owner,
			                             String message) {
		this(owner, message, null);
	}
	
	/**
	 * 閉じるボタンと例外のスタックトレースを表示した
	 * テキストエリアとユーザへのメッセージで構成される
	 * モーダルダイアログを表示する。<br/>
	 * 最上部にメッセージが表示され、その下にテキストエリア、
	 * 最下部にボタンが配置される。<br/>
	 * メッセージに改行(\n \r \r\n)が含まれる場合には
	 * 改行して表示される。<br/>
	 * message が null の場合には何も表示されない。<br/>
	 * ownerは表示するモーダルダイアログの親ダイアログ。
	 * 
	 * @param owner 親ダイアログ。nullでもよい。
	 * @param message ダイアログに表示するメッセージ
	 */
	public SwingUncaughtExceptionHandler(Dialog owner,
			                             String message) {
		this(owner, message, null);
	}
	
	/**
	 * 閉じるボタンと例外のスタックトレースを表示した
	 * テキストエリアとユーザへのメッセージで構成される
	 * モーダルダイアログを表示する。<br/>
	 * 最上部にメッセージが表示され、その下にテキストエリア、
	 * 最下部にボタンが配置される。<br/>
	 * メッセージに改行(\n \r \r\n)が含まれる場合には
	 * 改行して表示される。<br/>
	 * 表示されるダイアログは引数の dialogInfo をもとに
	 * 生成される。<br/>
	 * 引数の dialogInfo のフィールド message が
	 * メッセージとして表示される。<br/>
	 * dialogInfo の contens の内容は無視される。<br/>
	 * message が null の場合には何も表示されない。<br/>
	 * 引数が null の場合にはデフォルトコンストラクタと等価で
	 * 例外はスローしない。<br/>
	 * ownerは表示するモーダルダイアログの親フレーム。
	 * 
	 * @param owner 親フレーム。nullでもよい。
	 * @param dialogInfo ダイアログに関する設定情報を保持する
	 *        MessageDialog.DialogInfo オブジェクト
	 */
	public SwingUncaughtExceptionHandler(
								Frame owner,
			                    MessageDialog.DialogInfo dialogInfo) {
		this(owner, null, dialogInfo);
	}
	
	/**
	 * 閉じるボタンと例外のスタックトレースを表示した
	 * テキストエリアとユーザへのメッセージで構成される
	 * モーダルダイアログを表示する。<br/>
	 * 最上部にメッセージが表示され、その下にテキストエリア、
	 * 最下部にボタンが配置される。<br/>
	 * メッセージに改行(\n \r \r\n)が含まれる場合には
	 * 改行して表示される。<br/>
	 * 表示されるダイアログは引数の dialogInfo をもとに
	 * 生成される。<br/>
	 * 引数の dialogInfo のフィールド message が
	 * メッセージとして表示される。<br/>
	 * dialogInfo の contens の内容は無視される。<br/>
	 * message が null の場合には何も表示されない。<br/>
	 * 引数が null の場合にはデフォルトコンストラクタと等価で
	 * 例外はスローしない。<br/>
	 * ownerは表示するモーダルダイアログの親ダイアログ。
	 * 
	 * @param owner 親ダイアログ。nullでもよい。
	 * @param dialogInfo ダイアログに関する設定情報を保持する
	 *        MessageDialog.DialogInfo オブジェクト
	 */
	public SwingUncaughtExceptionHandler(
								Dialog owner,
								MessageDialog.DialogInfo dialogInfo) {
		this(owner, null, dialogInfo);
	}
	
	private SwingUncaughtExceptionHandler(
			                    Frame owner,
			                    String message, 
			                    MessageDialog.DialogInfo dialogInfo) {
		ownerDialog = null;
		ownerFrame = owner;
		if (dialogInfo == null) {
			this.dialogInfo = new MessageDialog.DialogInfo();
			this.dialogInfo.message = message;
			this.dialogInfo.buttonLabels = new String[]{"閉じる"};
			this.dialogInfo.title = "想定外エラー";
		} else {
			this.dialogInfo = copyInfo(dialogInfo);
		}
	}
	
	private SwingUncaughtExceptionHandler(
                                Dialog owner,
                                String message, 
                                MessageDialog.DialogInfo dialogInfo) {
		ownerFrame = null;
		ownerDialog = owner;
		if (dialogInfo == null) {
			this.dialogInfo = new MessageDialog.DialogInfo();
			this.dialogInfo.message = message;
			this.dialogInfo.buttonLabels = new String[]{"閉じる"};
			this.dialogInfo.title = "想定外エラー";
		} else {
			this.dialogInfo = copyInfo(dialogInfo);
		}
	}
	
	
	
	public void uncaughtException(Thread t, Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(
				                 new BufferedWriter(stringWriter));
		e.printStackTrace(writer);
		writer.close();
		dialogInfo.contents = stringWriter.getBuffer().toString();
		if (ownerDialog != null) {
			MessageDialog.showMessageDialog(ownerDialog, dialogInfo);
		} else if (ownerFrame != null) {
			MessageDialog.showMessageDialog(ownerFrame, dialogInfo);
		} else {
			MessageDialog.showMessageDialog(dialogInfo);
		}
		
	}
	
	private MessageDialog.DialogInfo copyInfo(
									MessageDialog.DialogInfo info) {
		MessageDialog.DialogInfo newOne = new MessageDialog.DialogInfo();
		
		newOne.buttonAddedBottom = info.buttonAddedBottom;
		newOne.columns = info.columns;
		newOne.contents = info.contents;
		newOne.maxColumns = info.maxColumns;
		newOne.maxRows = info.maxRows;
		newOne.maxHeight = info.maxHeight;
		newOne.maxWidth = info.maxWidth;
		newOne.message = info.message;
		newOne.rows = info.rows;
		newOne.title = info.title;
		newOne.tabCount = info.tabCount;
		
		newOne.buttonLabels = info.buttonLabels;
		if (info.buttonLabels != null) {
			newOne.buttonLabels = new String[info.buttonLabels.length];
			System.arraycopy(info.buttonLabels, 0, 
					newOne.buttonLabels, 0, newOne.buttonLabels.length);
		}
		
		return newOne;
	}
	
	
	/*
	//てすと
	public static void main(String[] args) {
		
		Thread.setDefaultUncaughtExceptionHandler(
				new SwingUncaughtExceptionHandler(
						(Frame)null, "アプリケーションの起動に失敗しました。"));
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button = new JButton("throws exception");
		button.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
					throw new RuntimeException("テスト例外");
				}
			}
		);
		frame.getContentPane().add(button);
		frame.setSize(300, 300);
		frame.setVisible(true);
		Thread.setDefaultUncaughtExceptionHandler(
				new SwingUncaughtExceptionHandler(
						frame, "想定外の例外が発生しました。"));
	}	
	*/
}