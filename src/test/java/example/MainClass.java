package example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

public class MainClass extends JPanel {
	public MainClass() {
		super(new BorderLayout());
		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		JPanel topPanel = new JPanel();
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu();
		menu.setText("Style");
		Action bold = new BoldAction();
		bold.putValue(Action.NAME, "Bold");
		menu.add(bold);
		Action italic = new ItalicAction();
		italic.putValue(Action.NAME, "Italic");
		menu.add(italic);
		Action foreground = new ForegroundAction();
		foreground.putValue(Action.NAME, "Color");
		menu.add(foreground);
		Action fontAndSize = new FontAndSizeAction();
		fontAndSize.putValue(Action.NAME, "Font and Size");
		menu.add(fontAndSize);
		bar.add(menu);
		topPanel.add(bar);
		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		setPreferredSize(new Dimension(500, 300));
	}

	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
		JFrame frame = new JFrame("@title@");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainClass());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}

class BoldAction extends StyledEditorKit.StyledTextAction {
	private static final long serialVersionUID = 9174670038684056758L;

	public BoldAction() {
		super("font-bold");
	}

	public String toString() {
		return "Bold";
	}

	public void actionPerformed(ActionEvent e) {
		JEditorPane editor = getEditor(e);
		if (editor != null) {
			StyledEditorKit kit = getStyledEditorKit(editor);
			MutableAttributeSet attr = kit.getInputAttributes();
			boolean bold = (StyleConstants.isBold(attr)) ? false : true;
			SimpleAttributeSet sas = new SimpleAttributeSet();
			StyleConstants.setBold(sas, bold);
			setCharacterAttributes(editor, sas, false);
		}
	}
}

class ItalicAction extends StyledEditorKit.StyledTextAction {
	private static final long serialVersionUID = -1428340091100055456L;

	public ItalicAction() {
		super("font-italic");
	}

	public String toString() {
		return "Italic";
	}

	public void actionPerformed(ActionEvent e) {
		JEditorPane editor = getEditor(e);
		if (editor != null) {
			StyledEditorKit kit = getStyledEditorKit(editor);
			MutableAttributeSet attr = kit.getInputAttributes();
			boolean italic = (StyleConstants.isItalic(attr)) ? false : true;
			SimpleAttributeSet sas = new SimpleAttributeSet();
			StyleConstants.setItalic(sas, italic);
			setCharacterAttributes(editor, sas, false);
		}
	}
}

class ForegroundAction extends StyledEditorKit.StyledTextAction {
	private static final long serialVersionUID = 6384632651737400352L;
	JColorChooser colorChooser = new JColorChooser();
	JDialog dialog = new JDialog();
	boolean noChange = false;
	boolean cancelled = false;

	public ForegroundAction() {
		super("foreground");
	}

	public void actionPerformed(ActionEvent e) {
		JTextPane editor = (JTextPane) getEditor(e);
		if (editor == null) {
			JOptionPane
					.showMessageDialog(
							null,
							"You need to select the editor pane before you can change the color.",
							"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int p0 = editor.getSelectionStart();
		StyledDocument doc = getStyledDocument(editor);
		Element paragraph = doc.getCharacterElement(p0);
		AttributeSet as = paragraph.getAttributes();
		fg = StyleConstants.getForeground(as);
		if (fg == null) {
			fg = Color.BLACK;
		}
		colorChooser.setColor(fg);
		JButton accept = new JButton("OK");
		accept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fg = colorChooser.getColor();
				dialog.dispose();
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cancelled = true;
				dialog.dispose();
			}
		});
		JButton none = new JButton("None");
		none.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				noChange = true;
				dialog.dispose();
			}
		});
		JPanel buttons = new JPanel();
		buttons.add(accept);
		buttons.add(none);
		buttons.add(cancel);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(colorChooser, BorderLayout.CENTER);
		dialog.getContentPane().add(buttons, BorderLayout.SOUTH);
		dialog.setModal(true);
		dialog.pack();
		dialog.setVisible(true);
		if (!cancelled) {
			MutableAttributeSet attr = null;
			if (editor != null) {
				if (fg != null && !noChange) {
					attr = new SimpleAttributeSet();
					StyleConstants.setForeground(attr, fg);
					setCharacterAttributes(editor, attr, false);
				}
			}
		}// end if color != null
		noChange = false;
		cancelled = false;
	}

	private Color fg;
}

class FontAndSizeAction extends StyledEditorKit.StyledTextAction {
	private static final long serialVersionUID = 584531387732416339L;
	private String family;
	private float fontSize;
	JDialog formatText;
	private boolean accept = false;
	JComboBox<String> fontFamilyChooser;
	JComboBox<Float> fontSizeChooser;

	public FontAndSizeAction() {
		super("Font and Size");
	}

	public String toString() {
		return "Font and Size";
	}

	public void actionPerformed(ActionEvent e) {
		JTextPane editor = (JTextPane) getEditor(e);
		int p0 = editor.getSelectionStart();
		StyledDocument doc = getStyledDocument(editor);
		Element paragraph = doc.getCharacterElement(p0);
		AttributeSet as = paragraph.getAttributes();
		family = StyleConstants.getFontFamily(as);
		fontSize = StyleConstants.getFontSize(as);
		formatText = new JDialog(new JFrame(), "Font and Size", true);
		formatText.getContentPane().setLayout(new BorderLayout());
		JPanel choosers = new JPanel();
		choosers.setLayout(new GridLayout(2, 1));
		JPanel fontFamilyPanel = new JPanel();
		fontFamilyPanel.add(new JLabel("Font"));
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();
		fontFamilyChooser = new JComboBox<String>();
		for (int i = 0; i < fontNames.length; i++) {
			fontFamilyChooser.addItem(fontNames[i]);
		}
		fontFamilyChooser.setSelectedItem(family);
		fontFamilyPanel.add(fontFamilyChooser);
		choosers.add(fontFamilyPanel);
		JPanel fontSizePanel = new JPanel();
		fontSizePanel.add(new JLabel("Size"));
		fontSizeChooser = new JComboBox<Float>();
		fontSizeChooser.setEditable(true);
		fontSizeChooser.addItem(new Float(4));
		fontSizeChooser.addItem(new Float(8));
		fontSizeChooser.addItem(new Float(12));
		fontSizeChooser.addItem(new Float(16));
		fontSizeChooser.addItem(new Float(20));
		fontSizeChooser.addItem(new Float(24));
		fontSizeChooser.setSelectedItem(new Float(fontSize));
		fontSizePanel.add(fontSizeChooser);
		choosers.add(fontSizePanel);
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				accept = true;
				formatText.dispose();
				family = (String) fontFamilyChooser.getSelectedItem();
				fontSize = Float.parseFloat(fontSizeChooser.getSelectedItem()
						.toString());
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				formatText.dispose();
			}
		});
		JPanel buttons = new JPanel();
		buttons.add(ok);
		buttons.add(cancel);
		formatText.getContentPane().add(choosers, BorderLayout.CENTER);
		formatText.getContentPane().add(buttons, BorderLayout.SOUTH);
		formatText.pack();
		formatText.setVisible(true);
		MutableAttributeSet attr = null;
		if (editor != null && accept) {
			attr = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr, family);
			StyleConstants.setFontSize(attr, (int) fontSize);
			setCharacterAttributes(editor, attr, false);
		}
	}
}