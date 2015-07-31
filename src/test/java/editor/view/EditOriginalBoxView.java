package editor.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.Position;

public class EditOriginalBoxView extends BoxView {
	final Color color = Color.GRAY; // とりあえずの色

	public EditOriginalBoxView(Element elem, int y) {
		super(elem, y);
	}

	public void paint(Graphics g, Shape a) {
		super.paint(g, a);
		paintOriginalParagraph(g, a);
	}

	private void paintOriginalParagraph(Graphics g, Shape a) {
		try {
			Shape paragraph = modelToView(getEndOffset(), a,
					Position.Bias.Backward);
			Rectangle rec = (paragraph == null) ? a.getBounds() : paragraph
					.getBounds();
			final int X = rec.x;
			final int Y = rec.y;
			final int HEIGHT = rec.height;
			final Color defaultColor = g.getColor();

			g.setColor(color);
			g.drawString("[EOF]", X, Y + HEIGHT - 3);

			g.setColor(defaultColor);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		;

	}
}