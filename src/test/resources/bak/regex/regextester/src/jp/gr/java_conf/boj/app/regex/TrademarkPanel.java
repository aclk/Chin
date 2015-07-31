package jp.gr.java_conf.boj.app.regex;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

class TrademarkPanel extends JPanel {
	private final int marginH;
	private final int marginW;
	private final Image image;
	private final int imageHeight;
	private final int imageWidth;
	
	private boolean isEnabledSize;
	private int lastWidth;
	private int lastHeight;
	private int x;
	private int y;
	private int width;
	private int height;
	
	TrademarkPanel(ImageIcon imageIcon, String html,
	               int marginH, int marginW, int delayTimes) {
		if (delayTimes < 0) {
			throw new IllegalArgumentException(
				"delayTimes must be more than zero !");
		}
		if (imageIcon == null) {
			throw new IllegalArgumentException(
					"imageIcon is null !");
		}
		imageHeight = imageIcon.getIconHeight();
		imageWidth = imageIcon.getIconWidth();
		if ((imageHeight <= 0) || (imageWidth <= 0)) {
			throw new IllegalArgumentException(
				"imageIcon contains illegal image !");
		}
		image = imageIcon.getImage();
		this.marginH = marginH;
		this.marginW = marginW;
		setLayout(new BorderLayout());
		setToolTipText(html);
		ToolTipManager.sharedInstance().setDismissDelay(delayTimes);
	}
	
	/*
	// ルートペインの左上あたりに表示する場合にはオーバーライド
	public Point getToolTipLocation(MouseEvent event) {
		Point rootP = getRootPane().getLocationOnScreen();
		Point p = getLocationOnScreen();
		return new Point(rootP.x - p.x + 10, rootP.y - p.y + 10);
	}
	*/
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int currentWidth = getWidth();
		int currentHeight = getHeight();
		if ((currentWidth == lastWidth) && 
				(currentHeight == lastHeight) && isEnabledSize) {
			g.drawImage(image, x, y, width, height, this);
			return;
		}
		
		lastWidth = currentWidth;
		lastHeight = currentHeight;
		
		// 以下最初の描画かサイズが変更されている場合
		width = currentWidth - marginW;
		height = currentHeight - marginW;
		if ((width < imageWidth) || (height < imageHeight)) {
			isEnabledSize = false;
			ToolTipManager.sharedInstance().setEnabled(false);
			return;
		}
		isEnabledSize = true;
		ToolTipManager.sharedInstance().setEnabled(true);
		int newHeight = (int)((float)width * imageHeight / imageWidth);
		if (newHeight <= height) {
			x = marginH;
			y = marginH + (height - newHeight) / 2;
			height = newHeight;
		} else {
			int newWidth = (int)((float)height * imageWidth / imageHeight);
			x = marginH + (width - newWidth) / 2;
			y = marginH;
			width = newWidth;
		}
		g.drawImage(image, x, y, width, height, this);
	}
	/*
	private static final String JAVA_COPYRIGHT = 
		"<html><body bgcolor=\"white\" text=\"black\">" +
		"Sun、Sun Microsystems、Java、および<br>" +
		"Java関連の商標は米国Sun Microsystems社の<br>" +
		"商標または登録商標です。<br><br><br><br><br><br><br><br>test" +
		"</body></html>";
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TrademarkPanel panel = new TrademarkPanel(
				Main.getImageIcon("mill.gif"), JAVA_COPYRIGHT, 10, 20,
				180000);
		frame.getContentPane().add(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
	*/
}
