package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.text.ViewFactory;

public class EditPanel extends JPanel implements ChangeListener,
		DocumentListener {
	// とりあえずの設定値
	private final Font defaultEditFont = new Font("ＭＳ ゴシック", Font.PLAIN, 16);
	private final int TAB_MAX = 50; // 正常に動作する１行のタブの数
	private JTextPane textPane, linePane;
	private EditHeadPanel headPane;
	private JViewport editView;
	private int beforeMaxLine;
	private JScrollPane editScroll;

	EditPanel() {
		super(new BorderLayout());
		beforeMaxLine = 0;

		/* 編集領域の追加 */
		textPane = new JTextPane() {
			public boolean getScrollableTracksViewportWidth() {
				Object parent = getParent();
				if (parent instanceof JViewport) {
					if (ui.getPreferredSize(this).width < ((JViewport) parent)
							.getWidth()) {
						return true;
					}
				}
				return false;
			}
		};
		add(setDefaultTextPane(), BorderLayout.CENTER);

		/* 行数表示の追加 */
		linePane = new JTextPane();
		setDefaultLinePane();

		/* ヘッダの追加 */
		headPane = new EditHeadPanel();
		setDefaultHeadPane();
	}

	private JComponent setDefaultTextPane() {
		StyledEditorKit orgKit = new StyledEditorKit() {
			public ViewFactory getViewFactory() {
				return new EditOriginalViewFactory();
			}
		};
		textPane.setEditorKit(orgKit);
		textPane.setFont(defaultEditFont);

		textPane.getDocument().addDocumentListener(this);

		// とりあえずここで呼ぶ
		setTabs(textPane, 4);

		editScroll = new JScrollPane(textPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		editScroll.setBackground(Color.WHITE);
		editScroll.setBorder(new LineBorder(Color.WHITE));

		editView = editScroll.getViewport();
		editView.addChangeListener(this);

		Border border = BorderFactory.createMatteBorder(0, 1, 0, 0,
				Color.LIGHT_GRAY);
		editScroll.setViewportBorder(border);

		JPanel cornerPanel = new JPanel(new FlowLayout());
		cornerPanel.setBackground(Color.WHITE);
		cornerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.LIGHT_GRAY));
		editScroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerPanel);

		return editScroll;
	}

	private void setDefaultLinePane() {
		linePane.setFont(defaultEditFont);
		linePane.setEditable(false);
		linePane.setFocusable(false);
		linePane.setVisible(false);

		editScroll.setRowHeaderView(linePane);
	}

	private void setDefaultHeadPane() {
		// headPane.setBorder(new LineBorder(Color.WHITE));
		Border border = BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.LIGHT_GRAY);
		headPane.setBorder(border);

		editScroll.setColumnHeaderView(headPane);
	}

	public void setTabs(JTextPane targetPane, int tabSize) {
		Font tmpFont = targetPane.getFont();
		Font tabFont = new Font("monospaced", tmpFont.getStyle(),
				tmpFont.getSize());
		targetPane.setFont(tabFont);

		FontMetrics fm = getFontMetrics(tabFont);
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * tabSize;

		TabStop tabs[] = new TabStop[TAB_MAX];

		for (int i = 0; i < tabs.length; i++) {
			tabs[i] = new TabStop((i + 1) * tabWidth);
		}

		SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setTabSet(attr, new TabSet(tabs));
		StyleConstants.setFontSize(attr, tmpFont.getSize());
		StyleConstants.setFontFamily(attr, tmpFont.getFamily());
		int len = targetPane.getStyledDocument().getLength();
		targetPane.getStyledDocument().setParagraphAttributes(0, len, attr,
				true);
		targetPane.setFont(tmpFont);
	}

	public void setLinePane() {
		setLinePaneDocument();

		linePane.setVisible(true);
	}

	private void setLinePaneDocument() {
		int maxLine = getLineNumber();

		if (beforeMaxLine != maxLine) {
			beforeMaxLine = maxLine;
			int tarSpace = (int) Math.log10(maxLine);
			Document doc = new DefaultStyledDocument();
			SimpleAttributeSet attr = new SimpleAttributeSet();

			linePane.setDocument(doc);

			try {
				for (int i = 0; i < maxLine; i++) {
					/* Math.log10関連の修正箇所はここらへん */
					String str = String.format("%0" + (tarSpace + 1) + "d",
							i + 1);
					boolean space = true;
					for (int j = 0; j < str.length(); j++) {
						if (str.charAt(j) == '0' && space) {
							attr.addAttribute(StyleConstants.Foreground,
									Color.WHITE);
							space = true;
						} else {
							attr.addAttribute(StyleConstants.Foreground,
									Color.GRAY);
							space = false;
						}
						doc.insertString(doc.getLength(), "" + str.charAt(j),
								attr);
					}
					doc.insertString(doc.getLength(), "\n", attr);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	public void setHeadPane() {
		headPane.setEditorFontInfo(textPane.getFont().getSize() / 2,
				textPane.getMargin());
		headPane.setPreferredSize(new Dimension(textPane.getWidth(), 12));
		// とりあえず高さは12！

		headPane.repaint();
	}

	private int getLineNumber() {
		Document doc = textPane.getDocument();
		return doc.getDefaultRootElement().getElementIndex(doc.getLength()) + 1;
	}

	public void stateChanged(ChangeEvent event) {
		final int edit_x = editView.getViewPosition().x;
		headPane.setPreferredSize(new Dimension(editScroll.getWidth() + edit_x,
				headPane.getPreferredSize().height));
		editScroll.revalidate();
	}

	public void changedUpdate(DocumentEvent event) {
	}

	public void insertUpdate(DocumentEvent event) {
		setLinePaneDocument();
	}

	public void removeUpdate(DocumentEvent event) {
		setLinePaneDocument();
	}

	public void testMethod() {
		textPane.setText("これは\nテスト用の\n最初の文字列です。\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nおわり");
	}
}