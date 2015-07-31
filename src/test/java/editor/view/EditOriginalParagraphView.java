package editor.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;

public class EditOriginalParagraphView extends ParagraphView {
	final Color color = Color.GRAY;		// とりあえずの色

	public EditOriginalParagraphView(Element elem) {
		super(elem);
	}

	public void paint(Graphics g, Shape a) {
		super.paint(g, a);
	       if( getEndOffset() != getDocument().getEndPosition().getOffset() ) {
			// 最後は改行マークを表示しない
			paintOriginalParagraph(g, a);
		}
	}

	private void paintOriginalParagraph(Graphics g, Shape a) {
		try {
			Shape paragraph = modelToView(getEndOffset(), a, Position.Bias.Backward);
			Rectangle rec = (paragraph==null) ? a.getBounds() : paragraph.getBounds();
			final int X = rec.x;
			final int Y = rec.y;
			final int HEIGHT = rec.height;
			final int WIDTH = HEIGHT/2;
			final Color defaultColor = g.getColor();

			g.setColor(color);
			g.drawLine(X+1, Y+HEIGHT-4, X+WIDTH-4, Y+HEIGHT-4);
			g.drawLine(X+1, Y+HEIGHT-4, X+WIDTH/2-2, Y+HEIGHT*3/4);
			g.drawLine(X+WIDTH-4, Y+HEIGHT/2, X+WIDTH-4, Y+HEIGHT-4);

			g.setColor(defaultColor);
		} catch (BadLocationException e) {
			e.printStackTrace();
		};

	}
}