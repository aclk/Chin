package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JViewport;

public class EditHeadPanel extends JPanel {
	private int editorFontSize;
	private Insets editorMargin;
	private int linePaneWidth;
	private int height;

	EditHeadPanel() {
		editorFontSize = 0;
		editorMargin = new Insets(0, 0, 0, 0);
		linePaneWidth = 0;
		height = getPreferredSize().height;

		setBackground(Color.WHITE);
		setForeground(Color.GRAY);
	}

	public void setEditorFontInfo(int editorFontSize, Insets editorMargin) {
		this.editorFontSize = editorFontSize;
		this.editorMargin = editorMargin;
	}

	public void setLinePaneWidth(int linePaneWidth) {
		this.linePaneWidth = linePaneWidth;
	}

	public void setPreferredSize(Dimension dm) {
		super.setPreferredSize(dm);
		height = dm.height;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		final int viewPositionX = ((JViewport) getParent()).getViewPosition().x;
		final int WIDTH = getPreferredSize().width + viewPositionX;

		g2d.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 8));

		// メモリ
		if (editorFontSize > 0) {
			final int startIndex = viewPositionX / editorFontSize;
			final int startWidth = editorMargin.left;
			for (int i = startIndex; i < WIDTH / editorFontSize; i++) {
				int startHeight = height / 2;
				int memory = i % 10;

				if (memory == 0) {
					startHeight = height / 4;
				} else if (memory == 5) {
					startHeight = height / 3;
				} else if (memory == 1) {
					String number = "" + ((i - 1) % 100);
					g2d.drawString(number, startWidth + editorFontSize * i - 4,
							height - 2);
				}

				if (memory != 1) {
					g2d.drawLine(startWidth + editorFontSize * i, startHeight,
							startWidth + editorFontSize * i, height);
				}
			}
		}
	}
}