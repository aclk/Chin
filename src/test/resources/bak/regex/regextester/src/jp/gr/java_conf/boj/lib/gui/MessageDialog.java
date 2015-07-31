package jp.gr.java_conf.boj.lib.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 *
 * 
 */
public class MessageDialog extends JDialog implements ActionListener {
    //DialogInfoのbuttonLabelsを基に作成されるボタン。表示しなければnull。
    private JButton[] buttons;
    
    //押されたボタンのテキストを格納する。
    //ウィンドウの×ボタンを押した場合にはnull。
    private String result;
    
    public MessageDialog() {
        this(null, false);
    }
    
    public MessageDialog(DialogInfo info, boolean modal) {
        this((Frame)null, info, modal);
    }
    public MessageDialog(Frame parentFrame, DialogInfo info, boolean modal) {
        super(parentFrame, (info == null) ? "" : info.title, modal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        configure((info == null) ? new DialogInfo() : info);
    }

    public MessageDialog(Dialog parentDialog, DialogInfo info, boolean modal) {
        super(parentDialog, (info == null) ? "" : info.title, modal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        configure((info == null) ? new DialogInfo() : info);
    }


    protected final void configure(DialogInfo info) {

        //message = info.message;
        //contents = info.contents;
        buttons = createButtons(info);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        Component top = createTop(info);
        if (top != null) {
            panel.add(top, BorderLayout.NORTH);
        }
        
        Component center = createCenter(info);
        if (center != null) {
            panel.add(center, BorderLayout.CENTER);
        }
        
        Component bottom = createBottom(info);
        if (bottom != null) {
            panel.add(bottom, BorderLayout.SOUTH);
        }
        
        JPanel basePanel = new JPanel();
        basePanel.setOpaque(false);
        basePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets.top = 10;
        c.insets.left = 10;
        c.insets.bottom = 10;
        c.insets.right = 10;
        basePanel.add(panel, c);
        JScrollPane sp = new JScrollPane(basePanel);

        getContentPane().add(sp, BorderLayout.CENTER);
        //setResizable(false);
        pack();
        
        Dimension dimension = getSize();
        int maxHeight = info.getMaxHeight();
        int maxWidth = info.getMaxWidth();
        boolean isOver = false;
        if (dimension.height > maxHeight) {
        	dimension.height = maxHeight;
        	isOver = true;
        }
        if (dimension.width > maxWidth) {
        	dimension.width = maxWidth;
        	isOver = true;
        }
        if (isOver) {
        	setSize(dimension);
        }
    }
    
    private Component createBottom(DialogInfo info) {
        if (!info.buttonAddedBottom) {
            return null;
        } 
        //ボタンを右寄せで横並びに配置したBox
        Box buttonBox = createButtonBox();
        if (buttonBox == null) {
            return null;
        }
        Box verBox = Box.createVerticalBox();
        verBox.add(Box.createVerticalStrut(10));
        verBox.add(buttonBox);
        return verBox;
    }
    
    private Component createCenter(DialogInfo info) {
        if ((info.contents == null) || (info.contents.length() == 0)) {
            return null;
        }
        
        //タブがあると幅がうまく計算できないのでスペースに変換
        String contents = info.contents.replaceAll("\t", info.getTabString());
        JTextArea textArea = new JTextArea();
        textArea.setText(contents);
        textArea.setOpaque(false);
        textArea.setEditable(false);

        //最後に改行が続いているとまとめて捨てられる為。
        String text = contents + "\na";

        String[] lines = text.split("(\r\n|\r(?<!\n)|(?!\r)\n)");
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        
        //text 生成時に付けた最後の a を捨てる為
        int outside = lines.length - 1;
        int max = 0;
        for (int i = 0; i < outside; i++) {
            int width = fm.stringWidth(lines[i]);
            //System.out.println("lines[" + i + "]" + width + " : " + lines[i]);
            if (width > max) {
                max = width;
            }
        }
        
        //テキストエリアに必要な列数と行数
        int rows = lines.length - 1;
        int columns = max / fm.charWidth('m') + 1;
        /*
        System.out.println("length:" + max);
        System.out.println("rows:" + (lines.length - 1));
        System.out.println("columns:" + (max / fm.charWidth('m') + 1));
        */
        boolean spUsed = false;
        
        //テキストエリア(contentを表示)
        if ((info.rows > 0) && (info.columns > 0)) {
            if ((info.rows < rows) || (info.columns < columns)) {
                spUsed = true;
            }
            textArea.setRows(info.rows);
            textArea.setColumns(info.columns);
        } else {
            int infoMaxRows = info.getMaxRows();
            if (infoMaxRows < rows) {
                spUsed = true;
                textArea.setRows(infoMaxRows);
            } else {
                textArea.setRows(rows);
            }

            int infoMaxColumns = info.getMaxColumns();
            
            if (infoMaxColumns < columns) {
                spUsed = true;
                textArea.setColumns(infoMaxColumns);
            } else {
                textArea.setColumns(columns);
            }
        }
        //System.out.println(textArea.getColumns());
        //System.out.println(textArea.getRows());
        
         
         
        if (spUsed) {
            JScrollPane sp = new JScrollPane(textArea);
            sp.setOpaque(false);
            sp.getViewport().setOpaque(false);
            return sp;
        } else {
            textArea.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return textArea;
    }
    
    private Component createTop(DialogInfo info) {
        //メッセージをラベルで貼り付けたパネルを生成
        JPanel messagePanel = createMessagePanel(info);
        
        Box horBox = null;
        if (messagePanel != null) {
            horBox = Box.createHorizontalBox();
            horBox.add(messagePanel);
            horBox.add(Box.createHorizontalStrut(15));
            horBox.add(Box.createGlue());            
        }

        //ボタン無しの場合
        if (info.buttonAddedBottom) {
            if (horBox == null) {
                return null;
            }
        } else {
            //ボタンを右寄せで横並びに配置したBox
            Box buttonBox = createButtonBox();
            if (horBox == null) {
                horBox = buttonBox;
            } else {
                horBox.add(buttonBox);
            }
        }
        Box verBox = Box.createVerticalBox();
        verBox.add(horBox);
        verBox.add(Box.createVerticalStrut(10));
        return verBox;            
    }
    
    private Box createButtonBox() {
        if (buttons == null) {
            return null;
        }
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createGlue());
        for (int i = 0; i < buttons.length; i++) {
            buttonBox.add(Box.createHorizontalStrut(15)); 
            
            Box box = Box.createVerticalBox();
            box.add(Box.createVerticalGlue());
            box.add(buttons[i]);
            buttonBox.add(box);
        }
        buttonBox.add(Box.createHorizontalStrut(15));
        return buttonBox;
    }
    
    //DialogInfoからボタンの生成
    private JButton[] createButtons(DialogInfo info) {
        if ((info.buttonLabels == null) || (info.buttonLabels.length == 0)) {
            return null;
        } 
        
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < info.buttonLabels.length; i++) {
            if ((info.buttonLabels[i] != null) && 
                    	(info.buttonLabels[i].length() != 0)) {
                list.add(info.buttonLabels[i]);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
               
        int size = list.size();
        buttons = new JButton[size];
        Iterator<String> iter = list.iterator();
        int index = 0;
        while (iter.hasNext()) {
            JButton button = new JButton(iter.next());
            button.addActionListener(this);
            
            buttons[index++] = button;
        }
        return buttons;
    }
    
    //DialogInfoからメッセージパネルの作成
    private JPanel createMessagePanel(DialogInfo info) {
        if ((info.message == null) || (info.message.length() == 0)) {
            return null;
        }
        String message = info.message.replaceAll("\t", info.getTabString());
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String[] labels = message.split("(\r\n|\r(?<!\n)|(?!\r)\n)");
        JLabel jLabel = new JLabel("m");
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int maxSize = info.getMaxColumns() * fontMetrics.charWidth('m');
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < labels.length; i++) {
        	list.clear();
    		divide(labels[i], fontMetrics, maxSize, list);
    		for (String text : list) {
    			JLabel label = new JLabel(text);
                panel.add(label);
    		}
        }
        return panel;
    }
    
    private void divide(String text, FontMetrics fm, 
    		            int maxSize, LinkedList<String> list) {
    	//終了条件
    	if (fm.stringWidth(text) <= maxSize) {
    		list.add(text);
    		return;
    	}
    	int index = 0;
    	int totalWidth = 0;
    	while (index < text.length()) {
    		totalWidth += fm.charWidth(text.charAt(index));
    		if (totalWidth > maxSize) {
    			break;
    		}
    		index++;
    	}
    	list.add(text.substring(0, index));
    	if (index < text.length()) {
    		divide(text.substring(index), fm, maxSize, list);
    	}
    }
    

    public static void showMessageDialog(DialogInfo info) {
        MessageDialog dialog = new MessageDialog(info, true);
        dialog.setVisible(true);
    }
    
    public static void showMessageDialog(Frame owner, DialogInfo info) {
        MessageDialog dialog = new MessageDialog(owner, info, true);
        dialog.setVisible(true);
    }
    
    public static void showMessageDialog(Dialog owner,DialogInfo info) {
        MessageDialog dialog = new MessageDialog(owner, info, true);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); 
        for (int i = 0; i < buttons.length; i++) {
            if (source == buttons[i]) {
                result = buttons[i].getText();
                break;
            }
        }
        dispose();
    }
    
    public String getResult() {
        return result;
    }

    public static final class DialogInfo implements Serializable {
    	
    	/**
    	 * message や contents にタブが含まれる場合には
    	 * スペースに置き換えられるが、その際にデフォルトで
    	 * １つのタブに置き換えるスペースの数
    	 */
    	public static final int DEFAULT_TAB_COUNT = 4;
    	
        /**
         * maxColumnsの値が0以下の場合に使用されるcontentsを
         * 表示するテキストエリアのデフォルトの最大列数
         */
        public static final int DEFAULT_MAX_COLUMNS = 60;
        
        /**
         * maxRowsの値が0以下の場合に使用されるcontentsを
         * 表示するテキストエリアのデフォルトの最大行数
         */        
        public static final int DEFAULT_MAX_ROWS = 20;
        
        /**
         * デフォルトのダイアログの最大高さ
         */
        public static final int DEFAULT_MAX_HEIGHT = 600;
        
        /**
         * デフォルトのダイアログの最大幅
         */
        public static final int DEFAULT_MAX_WIDTH = 800;
        /**
         * ダイアログのフレームタイトル。<br>
         * タイトルがいらなければ空の文字列でよい。<br>
         * デフォルトはnull。<br>
         */
        public String title;
        
        /**
         * 最上部左寄せで表示されるメッセージとなる文字列。<br>
         * 文字列内に改行文字(\n \r \r\n)が含まれている場合には
         * 改行して表示される。<br>
         * １行の文字数がDEFAULT_MAX_COLUMNSを超えている場合には
         * 勝手に改行して表示される。<br>
         * タブを含んでいる場合にはスペースに変換されるが、
         * スペースの数はtabCountで指定する。<br>
         * 何も表示しない場合にはnullでよい。<br>
         * デフォルトはnull。
         */
        public String message;
        
        /**
         * ダイアログのメッセージの下のテキストエリアに
         * 表示される何らかの文字列。<br>
         * rowsフィールドとcolumnsフィールドの両方に1以上の値を
         * 設定しない場合にはテキストエリアの列数はこのフィールドの
         * 文字列の一番長い行を基に計算され、
         * 行数はこのフィールドの文字列に含まれる改行(\n \r \r\n)の
         * 数となる。<br>
         * 何も表示しない場合にはnullでよい。<br>
         * rows, columns の設定に関わらずこのフィールドがnull
         * または空の文字列の場合にはテキストエリアは表示されない。<br>
         * テキストエリアはラインラップを行わないので表示可能になるように
         * 適切に改行(\n \r \r\n)を含めるべき。<br>
         * デフォルトはnull。
         */
        public String contents;
        
        /**
         * ダイアログに表示するボタンの数だけそのテキストを設定する。<br>
         * 配列にnullや空の文字列がある場合には無視される。<br>
         * １つもボタンを表示させない場合にはnullでよい。<br>
         * デフォルトはnull。
         */
        public String[] buttonLabels;

        /**
         * ボタン群を上部のメッセージの右に並べるならfalseを設定する。<br>
         * 最下部に並べる場合にはtrueを設定する。<br>
         * デフォルトはtrueで最下部に並べて表示する。<br>
         * ボタンが１つもなければこの設定は使用されない。<br>
         * デフォルトはtrueで最下部にボタンを配置する
         */
        public boolean buttonAddedBottom = true;
        

        /**
         * contentフィールドに設定した文字列を表示するテキストエリアの
         * 行数を設定する。<br>
         * このフィールドとcolumnsフィールド値が共に1以上の場合に
         * テキストエリアに反映される。<br>
         * contentフィールドがnullや空の文字列の場合には常に
         * テキストエリアは表示されない。
         */
        public int rows;

        /**
         * rowsフィールドを設定しない場合にはcontentsの最大行数で
         * テキストエリアが作成されるが行数が多い場合には
         * 画面に表示仕切れないコンポーネントになってしまうので、
         * 最大の行数をこのフィールドに設定しておく。<br>
         * デフォルトは0だがこのフィールドの値が0以下の場合には
         * このクラスの定数DEFAULT_MAX_ROWSの値が使用される。
         */
        public int maxRows;

        /**
         * contentフィールドに設定した文字列を表示するテキストエリアの
         * 行数を設定する。<br>
         * このフィールドとcolumnsフィールド値が共に1以上の場合に
         * テキストエリアに反映される。<br>
         * contentフィールドがnullや空の文字列の場合には常に
         * テキストエリアは表示されない。
         */
        public int columns;
        
        /**
         * columnsフィールドを設定しない場合にはcontentsの文字列の中で
         * 一番長い行に合わせてテキストエリアが作成されるが
         * １行がとても長い場合には画面に表示仕切れないコンポーネントに
         * なってしまうので、最大の列数をこのフィールドに設定しておく。<br>
         * デフォルトは0だがこのフィールドの値が0以下の場合には
         * このクラスの定数DEFAULT_MAX_COLUMNSの値が使用される。<br>
         * メッセージの１行の最大文字数もこのフィールドとなる。
         */
        public int maxColumns;

        /**
         * ダイアログの最大の高さ
         * これを超えるとスクロールバーを使う。<br>
         * この値が0以下の場合にはDEFAULT_MAX_HEIGHTが
         * 最大高さとなる。
         */
        public int maxHeight;
        
        /**
         * ダイアログの最大の幅
         * これを超えるとスクロールバーを使う。<br>
         * この値が0以下の場合にはDEFAULT_MAX_WIDTHが
         * 最大幅となる。
         */
        public int maxWidth;
        
        /**
         * messageやcontentsに含まれるタブを半角スペース何個分に変換
         * するかを指定する。<br>
         * デフォルトはDEFAULT_TAB_COUNT個分の半角スペースに変換される。<br>
         * 0以上の値が有効で負の数に設定した場合には
         * DEFAULT_TAB_COUNT個分の半角スペースに変換される。
         */
        public int tabCount = DEFAULT_TAB_COUNT;
        
        String getTabString() {
        	int count = (tabCount < 0) ? DEFAULT_TAB_COUNT : tabCount;
        	if (count == 0) {
        		return "";
        	}
        	if (count == 1) {
        		return " ";
        	}
        	
        	StringBuilder sb = new StringBuilder(count);
        	for (int i = 0; i < count; i++) {
        		sb.append(' ');
        	}
        	return sb.toString();
        }
        
        int getMaxColumns() {
        	return (maxColumns > 0) ? maxColumns : DEFAULT_MAX_COLUMNS;
        }
        
        int getMaxRows() {
        	return (maxRows > 0) ? maxRows : DEFAULT_MAX_ROWS;
        }
        
        int getMaxWidth() {
        	return (maxWidth > 0) ? maxWidth : DEFAULT_MAX_WIDTH;
        }
        
        int getMaxHeight() {
        	return (maxHeight > 0) ? maxHeight : DEFAULT_MAX_HEIGHT;
        }
    }
    
    /*
    //てすと
    public static void main(String[] args) {
        MessageDialog.DialogInfo info = new MessageDialog.DialogInfo();
        info.title = "タイトル";
        
        //info.message = "メッセージ\naaaaa\r\nbbbb\rcccc\n\n\nbbb";
        info.message = "メッセージ\nメッセージ\nメッセージ";
        
        info.contents = "あいうえお１２３４５６７８９０あいうえお１あいうえお１２３４５６７８９０あいうえお１２３４５６７８９０" + 
            //+ "\nmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
             //"mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
            "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM"
            + "\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17" +
            		"\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30";
         
        //info.contents = "contents";
        //info.buttonLabels = new String[]{"aaa", "bbb", "ccc", "ddd"};

        info.buttonLabels = new String[]{"aaa", "bbb"};
        
        //info.buttonAddedBottom = false;
        //info.rows = 1;
        //info.columns = 1;
        //info.maxRows = 15;
        //info.maxColumns = 35;
        MessageDialog.showMessageDialog(info);
        
        //System.out.println("end");
    }
    */

}
