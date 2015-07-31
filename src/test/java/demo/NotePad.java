package demo;

//目前实现了可以将文本内容读入到文本区并显示的功能
//2007-10-11实现了保存文件的功能
//问题一:需要解决当改变框架大小时,PANLE及时刷新的问题.
//问题二:没有实现当打开文件时,记事本的标题改变为文件名称的功能.
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NotePad {
	public static void main(String args[]) {
		JFrame frame = new NoteFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class NoteFrame extends JFrame {
	public NoteFrame() {// 在frame中不指定框架大小,因为是用NotePanel对象填充的.
		this.setLocation(200, 200);
		setTitle(title);
		Container c = getContentPane();
		p = new TextPanel();
		area = p.area;
		JScrollPane scroll = new JScrollPane(area);
		p.add(scroll);// 当文本内容超过文本区显示范围时,为文本区增加滚动条.
		add(p);
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
		// 增加"文件"菜单
		JMenu fileMenu = new JMenu("文件");
		JMenuItem openItem = new JMenuItem("打开");
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.showOpenDialog(NoteFrame.this);
				filePath = chooser.getSelectedFile().getPath();
				try {
					File f = new File(filePath);
					BufferedInputStream in = new BufferedInputStream(
							new FileInputStream(f));
					int n = in.available();
					byte[] b = new byte[n];
					in.read(b, 0, n);
					// in.close();
					fileText = new String(b);
					area.setText(fileText);
				} catch (IOException e1) {
					System.out.println(e.toString());
				}
			}
		});
		fileMenu.add(openItem);
		// 添加保存功能
		JMenuItem saveItem = new JMenuItem("保存");
		saveItem.addActionListener(new ActionListener() {// 处理保存事件
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();// 创建一个jFileChooser的实例
				chooser.setCurrentDirectory(new File("."));// 设定默认目录为当前目录
				chooser.showSaveDialog(NoteFrame.this);// 显示保存文件对话框
				String filePath = chooser.getSelectedFile().getPath();// 获取输入的文件名
				try {
					FileWriter out = new FileWriter(new File(filePath));
					System.out.println(area.getText());
					out.write(area.getText());
					out.flush();
				} catch (IOException e1) {
					System.out.println(e1.toString());
				}
			}
		});
		fileMenu.add(saveItem);
		// 增加退出功能
		JMenuItem exitItem = new JMenuItem("退出");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		// 增加"帮助"菜单
		JMenu helpMenu = new JMenu("帮助");
		JMenuItem aboutItem = new JMenuItem("关于");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "小马菜菜倾情制作 2007-10-11");
			}
		});
		helpMenu.add(aboutItem);
		menubar.add(fileMenu);// 将文件菜单添加到菜单栏
		menubar.add(helpMenu);// 将帮助菜单添加到菜单栏
		pack();// 使用该语句,使panel的大小与frame相适应
		area.setText(fileText);
	}

	public static String fileText, filePath;
	public static JTextArea area;
	public static String title;
	public static TextPanel p;
}

class TextPanel extends JPanel {
	public TextPanel() {
		setSize(600, 800);
		area = new JTextArea(20, 40);
		this.setLayout(new BorderLayout());
		add(area, BorderLayout.CENTER);
	}

	public static JTextArea area;
	public static String title;
}
