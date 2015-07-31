package demo.buffer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hoge {
	Logger log = LoggerFactory.getLogger(Hoge.class);

	// @Test
	public void rot13Input() throws IOException {
		// ユーザからのテキスト行を読むためにBufferedReaderを設定します。
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream("/tmp/test.txt")));
		for (;;) { // 無限ループ
			System.out.print("> "); // プロンプトを表示します。
			String line = in.readLine(); // 1行読みます。
			if ((line == null) || line.equals("quit")) // EOFあるいは"quit"の場合
				break; // ループから抜け出します。
			StringBuffer buf = new StringBuffer(line); // StringBufferを使います。
			for (int i = 0; i < buf.length(); i++)
				// 各文字に対して、
				buf.setCharAt(i, rot13(buf.charAt(i))); // 暗号化したものを保存
			System.out.println(buf); // 暗号化された行を表示します。
		}
		in.close();
	}

	/**
	 * このメソッドはRot13という暗号化を実行します。各文字を13個後の文字で置き換えます。
	 * Latinコードのアルファベットは26文字なので、このメソッドは符号化(暗号化)にも復号 化にも使えます。
	 */
	public static char rot13(char c) {
		if ((c >= 'A') && (c <= 'Z')) { // 大文字の場合、
			c += 13; // 各文字を13個後の文字で置き換えます。
			if (c > 'Z')
				c -= 26; // Zより大きくなったら、26だけ減算します。
		}
		if ((c >= 'a') && (c <= 'z')) { // 小文字についても同様です。
			c += 13;
			if (c > 'z')
				c -= 26;
		}
		return c; // 変換後の文字列を返します。
	}

	@Test
	public void testHoge() {
		CharBuffer cb = CharBuffer.allocate(64);
		dump("allocate(64)", cb);
		cb.limit(16);
		dump("limit(16)", cb);
		cb.put("abcde".toCharArray());
		dump("put(\"abcde\")", cb);
		cb.rewind();
		dump("rewind()", cb);
		log.info("read buffer");
		dumpCharBuffer(cb);
		log.info("▽▽▽▽▽▽");
		try {
			log.info("put(\"ABC\")");
			cb.put("ABC".toCharArray());
			dump(cb);
		} catch (BufferOverflowException e) {
			log.info("Buffer Overflow!");
		}
		log.info("△△△△△△");
		cb.position(5);
		cb.mark();
		dump("position(5),mark()", cb);
		cb.put("DEF".toCharArray());
		dump("put(\"DEF\")", cb);
		cb.reset();
		dump("reset()", cb);
		dumpCharBuffer(cb);
		cb.clear();
		dump("clear()", cb);
		cb.put("GHI".toCharArray());
		cb.flip();
		dump("put(\"GHI\"),flip()", cb);
		dumpCharBuffer(cb);
	}

	public void dumpCharBuffer(CharBuffer b) {
		log.info("■■■ read buffer ■■■");
		char c = '\0';

		while (b.hasRemaining()) {
			c = b.get();
			if (c != 0)
				dump("\'" + c + "\': ", b);
			else
				dump("\' \': ", b);
		}
	}

	public void dump(CharBuffer b) {
		log.info("capacity=\'" + b.capacity() + "\', position=\'"
				+ b.position() + "\', limit=\'" + b.limit()
				+ "\', remaining=\'" + b.remaining() + "\', length=\'"
				+ b.length() + "\'");
		// System.out.print("---> capacity\'" + b.capacity() + "\',");
		// System.out.print("position\'" + b.position() + "\',");
		// System.out.print("limit\'" + b.limit() + "\',");
		// System.out.print("remaining\'" + b.remaining() + "\',");
		// System.out.println("length\'" + b.length() + "\'");
	}

	public void dump(String str, CharBuffer b) {
		log.info(str);
		// System.out.println(str);
		log.info("capacity=\'" + b.capacity() + "\', position=\'"
				+ b.position() + "\', limit=\'" + b.limit()
				+ "\', remaining=\'" + b.remaining() + "\', length=\'"
				+ b.length() + "\'");
		// System.out.print("--- >capacity\'" + b.capacity() + "\',");
		// System.out.print("position\'" + b.position() + "\',");
		// System.out.print("limit\'" + b.limit() + "\',");
		// System.out.print("remaining\'" + b.remaining() + "\',");
		// System.out.println("length\'" + b.length() + "\'");
	}

	// @Test
	public void test() throws IOException {
		Charset cs = Charset.forName("MS932");

		FileInputStream fis = null;
		FileChannel fc = null;
		try {
			fis = new FileInputStream("/tmp/test.txt");
			fc = fis.getChannel();
			decode(fis, cs);
			decodeCh(fc, cs);
			decodeLump(fc, cs);
		} finally {
			// 取得したチャネルもクローズされる
			fis.close();
			fc.close();
		}
	}

	public void decode(InputStream is, Charset cs) throws IOException {
		CharsetDecoder decoder = cs.newDecoder();
		// ファイルの終わりまで読み込んだかどうか
		boolean end = false;
		// バッファーを用意する。
		// 今回は1文字ずつしか変換しないので、そんなに大きなバッファーは要らない
		// bbは書き込める状態
		ByteBuffer bb = ByteBuffer.allocateDirect(16);
		// cbは書き込める状態
		CharBuffer cb = CharBuffer.allocate(16);
		do {
			int d = is.read();
			if (d >= 0) {
				System.out.printf("%02x→", d);
				// bbへの書き込み
				bb.put((byte) d);
			} else {
				end = true;
			}
			// bbを読み込み状態に変更
			bb.flip();
			// bbから読み込み、cbへ書き込む
			CoderResult cr = decoder.decode(bb, cb, end);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			if (end) {
				// cbへ書き込む
				cr = decoder.flush(cb);
				if (!cr.isUnderflow()) {
					cr.throwException();
				}
			}
			// cbを読み込み状態に変更
			cb.flip();
			// cbに読み込めるデータがある場合
			while (cb.hasRemaining()) {
				// cbから読み込み
				char c = cb.get();
				System.out.printf("\'%c\'%n", c);
			}
			// bbに読み込めるデータがまだ残っている場合
			if (bb.hasRemaining()) {
				// データを詰めて書き込める状態に変更
				bb.compact();
			} else {
				// 書き込める状態に変更
				bb.clear();
			}
			// cbを書き込み状態に変更
			cb.clear();
		} while (!end);
	}

	public void decodeCh(FileChannel ch, Charset cs) throws IOException {
		CharsetDecoder decoder = cs.newDecoder();
		// ファイルの終わりまで読み込んだかどうか
		boolean end = false;
		// バッファーを用意する。
		// bbは書き込める状態
		ByteBuffer bb = ByteBuffer.allocateDirect(256);
		// cbは書き込める状態
		CharBuffer cb = CharBuffer.allocate(256);
		do {
			// bbへ書き込み
			int len = ch.read(bb);
			if (len < 0) {
				end = true;
			}
			// bbを読み込み状態に変更
			bb.flip();
			// bbから読み込み、cbへ書き込む
			CoderResult cr = decoder.decode(bb, cb, end);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}

			if (end) {
				// cbへ書き込む
				cr = decoder.flush(cb);
				if (!cr.isUnderflow()) {
					cr.throwException();
				}
			}
			// cbを読み込み状態に変更
			cb.flip();
			String str = cb.toString();
			System.out.println(str);
			// データを詰めて書き込める状態に変更
			bb.compact();
			// cbを書き込み状態に変更
			cb.clear();
		} while (!end);
	}

	public void decodeLump(FileChannel ch, Charset cs) throws IOException {
		CharsetDecoder decoder = cs.newDecoder();
		MappedByteBuffer mbb = ch.map(MapMode.READ_ONLY, 0, ch.size());
		// mbbは読み込める状態になっている
		// mbbから読み込んで、cbを返す
		CharBuffer cb = decoder.decode(mbb);
		// cbは読み込める状態になっている
		String str = cb.toString();
		System.out.println(str);
	}
}