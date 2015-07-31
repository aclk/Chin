package output;

import java.io.*;

/** 書き換えする親クラス */
public abstract class AbstractReplaceOutput {
    static final String TMP_SUFFIX = "tmp";
    
    public void replaceOutput(String prjRootPath,
                              String jdbcUrl,
                              String user,
                              String password,
                              String schema) throws Exception {
        if (user == null || user.length() == 0) throw new IllegalArgumentException("userを設定しろよ");
        if (password == null || password.length() == 0) password = user;
        if (schema == null || schema.length() == 0) schema = user;
        
        File f = new File(prjRootPath);
        if (!f.exists()) throw new IllegalArgumentException("場所あってるか？");
        
        // 対象ファイルを取得
        String out = getTargetFilePathName();
        File outputFile = new File(f, out);
        if (!outputFile.exists()) {
            // mainに無い場合はtest系から探す
            out = out.replaceAll("src/main", "src/test");
            File testOutFile = new File(f, out);
            if (!testOutFile.exists()) {
                throw new IllegalArgumentException("出力ファイル(" + outputFile + ")が無いぞ！（´・ω・`）");
            }
            outputFile = testOutFile;
        }
        
        File inputFile = new File(outputFile.getAbsolutePath() + TMP_SUFFIX);
        // 元のファイルをリネームして退避させておく
        outputFile.renameTo(inputFile);
        
        // 置換出力開始
        replaceOutput(inputFile, outputFile, jdbcUrl, user, password, schema);
        
        // 元のファイルを削除
        inputFile.delete();
    }
    
    abstract protected String getTargetFilePathName();
    
    abstract protected void replaceOutput(File inputFile,
                                          File outputFile,
                                          String jdbcUrl,
                                          String user,
                                          String password,
                                          String schema) throws Exception;
}
