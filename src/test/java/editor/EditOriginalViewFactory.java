package editor;

import javax.swing.text.AbstractDocument;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import editor.view.EditOriginalBoxView;
import editor.view.EditOriginalLabelView;
import editor.view.EditOriginalParagraphView;

public class EditOriginalViewFactory implements ViewFactory {

	public View create(Element elem) {
		String kind = elem.getName();

		if (kind != null) {
			if (kind.equals(AbstractDocument.ContentElementName)) {
				return new EditOriginalLabelView(elem);
			} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
				return new EditOriginalParagraphView(elem);
			} else if (kind.equals(AbstractDocument.SectionElementName)) {
				return new EditOriginalBoxView(elem, View.Y_AXIS);
			} else if (kind.equals(StyleConstants.ComponentElementName)) {
				return new ComponentView(elem);
			} else if (kind.equals(StyleConstants.IconElementName)) {
				return new IconView(elem);
			}
		}
		return new LabelView(elem);
	}
}
