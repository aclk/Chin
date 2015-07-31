package example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.rtf.RTFEditorKit;

public class TextPaneTest extends JFrame implements ActionListener,
		CaretListener {

	protected JTextPane textPane;

	protected DefaultStyledDocument doc;
	protected StyleContext sc;

	protected JToolBar toolBar;

	protected JComboBox comboFonts; /* Font選択 */
	protected JComboBox comboSizes; /* Fontサイズ */
	protected JToggleButton toggleB; /* 強調 */
	protected JToggleButton toggleI; /* 斜体 */
	protected JToggleButton toggleU; /* アンダーライン */
	protected JToggleButton toggleS; /* 取り消し線 */
	protected JComboBox comboColor; /* 前景色 */

	protected String currentFontName = "";
	protected int currentFontSize = 0;
	protected boolean flag = false;

	protected String[] colorHTML = { "#000000", "#0000FF", "#00FF00",
			"#00FFFF", "#FF0000", "#FF00FF", "#FFFF00", "#FFFFFF" };

	protected RTFEditorKit rtfEditor;

	public static void main(String[] args) {
		TextPaneTest test = new TextPaneTest();

		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}

	TextPaneTest() {
		setTitle("TextPaneTest Test");
		setBounds(10, 10, 500, 300);

		textPane = new JTextPane();
		JScrollPane scroll = new JScrollPane(textPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		getContentPane().add(scroll, BorderLayout.CENTER);

		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);

		textPane.setDocument(doc);
		textPane.addCaretListener(this);

		/* ファイル操作用RTFEditorKit */
		rtfEditor = new RTFEditorKit();

		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		initToolbar();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		/* 初期文書の読み込み */
		initDocument();
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File", true);
		menuBar.add(fileMenu);

		JMenuItem newItem = new JMenuItem("New");
		fileMenu.add(newItem);
		newItem.addActionListener(this);
		newItem.setActionCommand("newItem");

		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		openItem.addActionListener(this);
		openItem.setActionCommand("openItem");

		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		saveItem.addActionListener(this);
		saveItem.setActionCommand("saveItem");

		fileMenu.addSeparator();

		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		exitItem.addActionListener(this);
		exitItem.setActionCommand("exitItem");

		return menuBar;
	}

	protected void initDocument() {
		StringBuffer sb = new StringBuffer();
		sb.append("スタイル付きのテキストサンプルです。¥n");
		sb.append("スタイルを変えて表示しています。");

		try {
			/* 文書を挿入する */
			doc.insertString(0, new String(sb),
					sc.getStyle(StyleContext.DEFAULT_STYLE));
		} catch (BadLocationException ble) {
			System.err.println("初期文書の読み込みに失敗しました。");
		}
	}

	protected void initToolbar() {
		toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		/* フォント一覧の取得 */
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String familyName[] = ge.getAvailableFontFamilyNames();

		/* フォント選択用コンボボックス */
		comboFonts = new JComboBox(familyName);
		comboFonts.setMaximumSize(comboFonts.getPreferredSize());
		comboFonts.addActionListener(this);
		comboFonts.setActionCommand("comboFonts");
		toolBar.add(comboFonts);

		/* フォントサイズ選択用コンボボックス */
		comboSizes = new JComboBox(new String[] { "8", "9", "10", "11", "12",
				"14", "16", "18", "20", "22", "24", "26", "28", "36", "48",
				"72" });
		comboSizes.setMaximumSize(comboSizes.getPreferredSize());
		comboSizes.addActionListener(this);
		comboSizes.setActionCommand("comboSizes");
		toolBar.add(comboSizes);

		toolBar.addSeparator();

		/* 強調 選択用トグルボタン */
		toggleB = new JToggleButton("<html><b>B</b></html>");
		toggleB.setPreferredSize(new Dimension(26, 26));
		toggleB.addActionListener(this);
		toggleB.setActionCommand("toggleB");
		toolBar.add(toggleB);

		/* 斜体 選択用トグルボタン */
		toggleI = new JToggleButton("<html><i>I</i></html>");
		toolBar.add(toggleI);
		toggleI.addActionListener(this);
		toggleI.setActionCommand("toggleI");
		toggleI.setPreferredSize(new Dimension(26, 26));

		/* アンダーライン 選択用トグルボタン */
		toggleU = new JToggleButton("<html><u>U</u></html>");
		toolBar.add(toggleU);
		toggleU.addActionListener(this);
		toggleU.setActionCommand("toggleU");
		toggleU.setPreferredSize(new Dimension(26, 26));

		/* 取り消し線 選択用トグルボタン */
		toggleS = new JToggleButton("<html><s>S</s></html>");
		toolBar.add(toggleS);
		toggleS.addActionListener(this);
		toggleS.setActionCommand("toggleS");
		toggleS.setPreferredSize(new Dimension(26, 26));

		toolBar.addSeparator();

		/* 前景色用コンボボックス */
		DefaultComboBoxModel<String> colorModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < 8; i++) {
			/* 色つきのラベルを作成する */
			StringBuffer sb = new StringBuffer();
			sb.append("<html><font color=\"");
			sb.append(colorHTML[i]);
			sb.append("\">■</font></html>");

			colorModel.addElement(new String(sb));
		}
		comboColor = new JComboBox<String>(colorModel);
		comboColor.setMaximumSize(comboColor.getPreferredSize());
		comboColor.addActionListener(this);
		comboColor.setActionCommand("comboColor");
		toolBar.add(comboColor);
	}

	public void actionPerformed(ActionEvent e) {
		if (flag) {
			/* キャレット変更に伴うActionEventの場合はパスする */
			return;
		}

		String actionCommand = e.getActionCommand();

		if (actionCommand.equals("exitItem")) {
			System.exit(0);
		} else if ((actionCommand.equals("newItem"))
				|| (actionCommand.equals("openItem"))
				|| (actionCommand.equals("saveItem"))) {

			fileOperation(actionCommand);
		} else {
			MutableAttributeSet attr = new SimpleAttributeSet();

			if (actionCommand.equals("comboFonts")) {
				/* フォント変更 */
				String fontName = comboFonts.getSelectedItem().toString();
				StyleConstants.setFontFamily(attr, fontName);
			} else if (actionCommand.equals("comboSizes")) {
				/* フォントサイズ変更 */
				int fontSize = 0;
				try {
					fontSize = Integer.parseInt(comboSizes.getSelectedItem()
							.toString());
				} catch (NumberFormatException ex) {
					return;
				}

				StyleConstants.setFontSize(attr, fontSize);
			} else if (actionCommand.equals("toggleB")) {
				/* 強調 */
				StyleConstants.setBold(attr, toggleB.isSelected());
			} else if (actionCommand.equals("toggleI")) {
				/* 斜体 */
				StyleConstants.setItalic(attr, toggleI.isSelected());
			} else if (actionCommand.equals("toggleU")) {
				/* アンダーライン */
				StyleConstants.setUnderline(attr, toggleU.isSelected());
			} else if (actionCommand.equals("toggleS")) {
				/* 取り消し線 */
				StyleConstants.setStrikeThrough(attr, toggleS.isSelected());
			} else if (actionCommand.equals("comboColor")) {
				/* 前景色 */
				int col = comboColor.getSelectedIndex();
				int b = (col % 2) * 255;
				int g = ((col / 2) % 2) * 255;
				int r = ((col / 4) % 2) * 255;

				StyleConstants.setForeground(attr, new Color(r, g, b));
			} else {
				return;
			}

			setAttributeSet(attr);
		}

		textPane.requestFocusInWindow();
	}

	protected void fileOperation(String actionCommand) {
		JFileChooser chooser = new JFileChooser();
		RtfFilter filter = new RtfFilter();
		chooser.setFileFilter(filter);

		if (actionCommand.equals("newItem")) {
			/* 新規ファイル */
			doc = new DefaultStyledDocument(sc);
			textPane.setDocument(doc);
		} else if (actionCommand.equals("openItem")) {
			/* ファイルを開く */
			doc = new DefaultStyledDocument(sc);

			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File fChoosen = chooser.getSelectedFile();
			try {
				InputStream in = new FileInputStream(fChoosen);
				rtfEditor.read(in, doc, 0);
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			textPane.setDocument(doc);
		} else if (actionCommand.equals("saveItem")) {
			/* ファイルの保存 */
			if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File fChoosen = chooser.getSelectedFile();
			try {
				OutputStream out = new FileOutputStream(fChoosen);
				rtfEditor.write(out, doc, 0, doc.getLength());
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			return;
		}
	}

	protected void setAttributeSet(AttributeSet attr) {
		/* 選択している文字のスタイルを変更する */

		int start = textPane.getSelectionStart();
		int end = textPane.getSelectionEnd();

		doc.setCharacterAttributes(start, end - start, attr, false);
	}

	public void caretUpdate(CaretEvent e) {
		flag = true;

		int p = textPane.getSelectionStart();
		AttributeSet atrr = doc.getCharacterElement(p).getAttributes();

		String name = StyleConstants.getFontFamily(atrr);
		/* 変更前と同じ場合は無視する */
		if (!currentFontName.equals(name)) {
			currentFontName = name;
			comboFonts.setSelectedItem(name);
		}

		int size = StyleConstants.getFontSize(atrr);
		/* 変更前と同じ場合は無視する */
		if (currentFontSize != size) {
			currentFontSize = size;
			comboSizes.setSelectedItem(Integer.toString(size));
		}

		/* 強調/斜体/アンダーライン/取り消し線 の状態表示 */
		toggleB.setSelected(StyleConstants.isBold(atrr));
		toggleI.setSelected(StyleConstants.isItalic(atrr));
		toggleU.setSelected(StyleConstants.isUnderline(atrr));
		toggleS.setSelected(StyleConstants.isStrikeThrough(atrr));

		/* 前景色の状態表示 */
		Color col = StyleConstants.getForeground(atrr);
		int r = (col.getRed() / 255) * 4;
		int g = (col.getGreen() / 255) * 2;
		int b = col.getBlue() / 255;
		comboColor.setSelectedIndex(r + g + b);

		flag = false;
	}
}