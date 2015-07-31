package jp.gr.java_conf.boj.app.regex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * ホワイトスペースを表すイメージを作成するstaticメソッドを
 * 用意したクラス。<br>
 */
class WhitespaceImageFactory {
	static final int CR_LF_IMAGE_WIDTH = 4;
	static final int CR_LF_IMAGE_MIN_HEIGHT = 10;
	static final Color WHITESPACE_IMAGE_COLOR = 
	                                      new Color(160, 160, 160);
	static final Color ENDLINE_COLOR = new Color(32, 32, 32);
	
	/**
	 * 幅4 高さがheightのCRのBufferedImageを作成して返す。<br>
	 * 最小の高さは10で引数のheightに10より小さい値を指定した場合には
	 * その値は無視され高さは10になる。<br>
	 * 10よりも大きい値を指定した場合は戻り値のイメージの
	 * 高さは大きくなるが描画される領域は常に同じで透明な部分が
	 * 増えるだけとなる。
	 * 
	 * @param color 描画に使用する色
	 * @param height 10以上の高さを指定する。
	 * @return 改行コードCRを表すImageオブジェクト
	 * @exception IllegalArgumentException 引数のColorオブジェクトが
	 *                                     nullの場合
	 */
	static Image getCRImage(Color color, int height) {
		if (color == null) {
			throw new IllegalArgumentException("color is null !");
		}
		int margin = (height > CR_LF_IMAGE_MIN_HEIGHT) 
                     ? height - CR_LF_IMAGE_MIN_HEIGHT : 0;
		
		BufferedImage image = new BufferedImage(CR_LF_IMAGE_WIDTH, height,  
		                                        BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.createGraphics();
		g.setColor(color);
		g.drawLine(1, margin + 0, 3, margin + 0);
		g.drawLine(1, margin + 1, 1, margin + 3);
		g.drawLine(2, margin + 3, 3, margin + 3);
		g.drawLine(1, margin + 5, 1, margin + 9);
		g.drawLine(2, margin + 5, 3, margin + 5);
		g.drawLine(3, margin + 6, 3, margin + 7);
		g.drawLine(2, margin + 7, 2, margin + 8);
		g.drawLine(3, margin + 9, 3, margin + 9);
		g.dispose();
		
		return image;
	}
	
	/**
	 * 幅4 高さがasecntのLFのBufferedImageを作成して返す。<br>
	 * 最小の高さは10で引数のascentに10より小さい値を指定した場合には
	 * その値は無視され高さは10になる。<br>
	 * 10よりも大きい値を指定した場合は戻り値のイメージの
	 * 高さは大きくなるが描画される領域は常に同じで透明な部分が
	 * 増えるだけとなる。
	 * 
	 * @param color 描画に使用する色
	 * @param ascent 10以上の高さを指定する。
	 * @return 改行コードLFを表すImageオブジェクト
	 * @exception IllegalArgumentException 引数のColorオブジェクトが
	 *                                     nullの場合
	 */
	static Image getLFImage(Color color, int ascent) {
		if (color == null) {
			throw new IllegalArgumentException("color is null !");
		}
		
		int margin = (ascent > CR_LF_IMAGE_MIN_HEIGHT) 
                      ? ascent - CR_LF_IMAGE_MIN_HEIGHT : 0;
		
		BufferedImage image = new BufferedImage(CR_LF_IMAGE_WIDTH, ascent,  
		                                        BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.createGraphics();
		g.setColor(color);
		g.drawLine(1, margin + 0, 1, margin + 3);
		g.drawLine(2, margin + 3, 3, margin + 3);
		g.drawLine(1, margin + 5, 1, margin + 9);
		g.drawLine(2, margin + 5, 3, margin + 5);
		g.drawLine(2, margin + 7, 3, margin + 7);
		g.dispose();
		
		return image;
	}
	
	/**
	 * 幅width高さがheightの半角スペースを表すイメージを作成して返す。<br>
	 * 
	 * @param color 描画に使用する色
	 * @param width イメージの幅
	 * @param height イメージの高さ
	 * @exception IllegalArgumentException colorがnullもしくはwidthが0以下
	 *                                     もしくはheightが0以下
	 * @return 半角スペースを表すImageオブジェクト
	 */
	static Image getSpaceImage(Color color, int width, int height) {
		if ((width <= 0) || (height <= 0)) {
			throw new IllegalArgumentException(
					"(width <= 0) || (height <= 0)");
		}
		if (color == null) {
			throw new IllegalArgumentException("color is null !");
		}
		BufferedImage image = new BufferedImage(width, height, 
		                                        BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.setColor(color);
		
		// フォントのベースラインのy座標
		int baselineY = height - 1;
		
		if (width == 1) {
			// 幅1なら左縦線のみ
			g.drawLine(0, 3, 0, baselineY);
			
		} else {
			// 幅2以上なら1ドットあけて左縦線を引く
			// 幅3の場合はそれでおわり
			g.drawLine(1, 3, 1, baselineY);
			
			if (width == 4) {
				// 幅4の場合は左縦線に続けて右縦線も引く
				g.drawLine(2, 3, 2, baselineY);
				
			} else if (width > 4) {
				// 幅5以上なら横線も引く
				int lastX = width - 2;
				int preLastX = (lastX > 2) ? lastX - 1 : lastX;
				g.drawLine(2, 3, preLastX, 3);
				g.drawLine(2, baselineY, preLastX, baselineY);
				g.drawLine(lastX, 3, lastX, baselineY);
			}
		}
		g.dispose();
		return image;
	}
	
	/**
	 * 幅width高さがheightの全角スペースを表すイメージを作成して返す。<br>
	 * 
	 * @param color 描画に使用する色
	 * @param width イメージの幅
	 * @param height イメージの高さ
	 * @exception IllegalArgumentException colorがnullもしくはwidthが0以下
	 *                                     もしくはheightが0以下
	 * @return 全角スペースを表すImageオブジェクト
	 */
	static Image getWspaceImage(Color color, int width, int height) {
		if ((width <= 0) || (height <= 0)) {
			throw new IllegalArgumentException(
					"(width <= 0) || (height <= 0)");
		}
		if (color == null) {
			throw new IllegalArgumentException("color is null !");
		}
		
		BufferedImage image = new BufferedImage(width, height, 
		                                        BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.setColor(color);
		int baselineY = height - 1;
		int y = (baselineY % 2 == 0) ? 4 : 3;
		if (height < 3) {
			g.drawLine(0, 0, 0, 0);
		} else {
			if (height < 8) {
				y -= (8 - height);
			}
			if (width == 1) {
				for (int i = y + 2; i < baselineY; i += 2) {
					g.drawLine(0, i, 0, i);
				}
				
			} else {
				for (int i = y + 2; i < baselineY; i += 2) {
					g.drawLine(1, i, 1, i);
				}
				
				if (width == 4) {
					for (int i = y + 2; i < baselineY; i += 2) {
						g.drawLine(2, i, 2, i);
					}
					
				} else if (width > 4) {
					int x = (width % 2 == 0) ? width - 3 : width - 2;
					for (int i = 3; i < x; i += 2) {
						g.drawLine(i, y, i, y);
						g.drawLine(i, baselineY, i, baselineY);
					}
					for (int i = y + 2; i < baselineY; i += 2) {
						g.drawLine(x, i, x, i);
					}
				}
			}
		}
		g.dispose();
		return image;
	}
	
	/**
	 * 幅width高さがheightのタブを表すイメージを作成して返す。<br>
	 * 
	 * @param color 描画に使用する色
	 * @param width イメージの幅
	 * @param height イメージの高さ
	 * @exception IllegalArgumentException colorがnullもしくはwidthが0以下
	 *                                     もしくはheightが0以下
	 * @return タブを表すImageオブジェクト
	 */
	static Image getTabImage(Color color, int width, int height) {
		if ((width <= 0) || (height <= 0)) {
			throw new IllegalArgumentException(
					"(width <= 0) || (height <= 0)");
		}
		if (color == null) {
			throw new IllegalArgumentException("color is null !");
		}
		BufferedImage image = new BufferedImage(width, height, 
		                                        BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		g.setColor(color);
		
		// height - 1 がベースライン
		int y = height - 1 - (height / 5);
		
		if (width == 1) {
			g.drawLine(0, y, 0, y);
			y -= 2;
			g.drawLine(0, y, 0, y);
		} else if (width == 2) {
			g.drawLine(0, y, 0, y--);
			g.drawLine(1, y, 1, y--);
			g.drawLine(0, y, 0, y);
		} else if (width == 3) {
			g.drawLine(0, y, 0, y--);
			g.drawLine(1, y, 2, y--);
			g.drawLine(0, y, 0, y);
		} else {
			g.drawLine(1, y, 1, y--);
			g.drawLine(2, y, 3, y--);
			g.drawLine(1, y, 1, y);
		}
		g.dispose();
		return image;
	}
}
