package tips;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.io.FileUtils;

public class BeforeOprate {

	public static void main(String[] args) throws IOException {
		for (File src : new File("C:\\Users\\Pactera\\git\\java-swing-tips")
				.listFiles()) {
			if (src.isDirectory()) {
				File f = new File(src.getPath() + "\\src\\web\\screenshot.png");
				if (f.exists()) {
					copy(f, new File("D:\\tmp\\swing\\" + src.getName()
							+ ".png"));
				} else {
					System.err.println(String.format("[%-30s]", f.getName())
							+ " --- ×");
				}
			}
		}

		// copyTo();
	}

	private static void copyTo() {
		File dir = new File("C:\\Users\\Pactera\\git\\java-swing-tips");

		File[] fs = dir.listFiles();
		File f = fs[2];
		if (f.isDirectory()) {
			String b = String.format("[%-30s]", f.getName());
			File source = new File(f.getPath() + "\\src\\java\\example\\");
			if (source.exists()) {
				File dest = new File(
						"D:\\work\\Chin\\src\\test\\java\\example\\");
				dest.mkdirs();
				try {
					FileUtils.copyDirectory(source, dest);
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			} else {
				System.out.println(b + " --- ×");
			}
		}

	}

	private static void copy(File source, File dest) throws IOException {
		FileChannel ic = null;
		FileChannel oc = null;
		try {
			ic = new FileInputStream(source).getChannel();
			oc = new FileOutputStream(dest).getChannel();
			oc.transferFrom(ic, 0, ic.size());
		} finally {
			ic.close();
			oc.close();
		}
	}

	private static void fileWrite(File _file, String _text) {
		try {
			// FileWriterクラスをインスタンス化
			FileWriter fw = new FileWriter(_file);
			fw.write(_text);
			// ファイルを閉じる
			fw.close();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
}
