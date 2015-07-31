package test;

import java.io.File;
import java.util.Arrays;

public class FileSortUtil {
	// for test
	public static void main(String[] args) {
		// c:\\tmpにあるすべてのファイルを名称順でソートします

		// まず、c:\\tmpというフォルダを取得
		File dirFile = new File("c:\\tmp\\");

		// ソート
		File[] sortedFiles = listSortedFiles(dirFile);
		for (File file : sortedFiles) {
			System.err.println(file.getPath());
		}
	}

	// 指定するフォルダに対してファイルの名称順でソートを行います
	public static File[] listSortedFiles(File dirFile) {
		assert dirFile.isDirectory();

		// フォルダにあるすべてのファイルを取得します
		File[] files = dirFile.listFiles();

		// 比較機能（Comparableを実装）を備えるファイルラッパ
		FileWrapper[] fileWrappers = new FileWrapper[files.length];
		for (int i = 0; i < files.length; i++) {
			fileWrappers[i] = new FileWrapper(files[i]);
		}

		// ソートを行う
		Arrays.sort(fileWrappers);

		// ソートしたオブジェクトから戻り値にセット
		File[] sortedFiles = new File[files.length];
		for (int i = 0; i < files.length; i++) {
			sortedFiles[i] = fileWrappers[i].getFile();
		}

		return sortedFiles;
	}
}

class FileWrapper implements Comparable {
	/** File */
	private File file;

	public FileWrapper(File file) {
		this.file = file;
	}

	// 比較用メソッド
	public int compareTo(Object obj) {
		assert obj instanceof FileWrapper;

		FileWrapper castObj = (FileWrapper) obj;

		if (this.file.getName().compareTo(castObj.getFile().getName()) > 0) {
			return 1; // 大きい場合
		} else if (this.file.getName().compareTo(castObj.getFile().getName()) < 0) {
			return -1; // 小さい場合
		} else {
			return 0; // 等しい場合
		}
	}

	public File getFile() {
		return this.file;
	}
}