package output;

import java.io.*;

/** ������������e�N���X */
public abstract class AbstractReplaceOutput {
    static final String TMP_SUFFIX = "tmp";
    
    public void replaceOutput(String prjRootPath,
                              String jdbcUrl,
                              String user,
                              String password,
                              String schema) throws Exception {
        if (user == null || user.length() == 0) throw new IllegalArgumentException("user��ݒ肵���");
        if (password == null || password.length() == 0) password = user;
        if (schema == null || schema.length() == 0) schema = user;
        
        File f = new File(prjRootPath);
        if (!f.exists()) throw new IllegalArgumentException("�ꏊ�����Ă邩�H");
        
        // �Ώۃt�@�C�����擾
        String out = getTargetFilePathName();
        File outputFile = new File(f, out);
        if (!outputFile.exists()) {
            // main�ɖ����ꍇ��test�n����T��
            out = out.replaceAll("src/main", "src/test");
            File testOutFile = new File(f, out);
            if (!testOutFile.exists()) {
                throw new IllegalArgumentException("�o�̓t�@�C��(" + outputFile + ")���������I�i�L�E�ցE`�j");
            }
            outputFile = testOutFile;
        }
        
        File inputFile = new File(outputFile.getAbsolutePath() + TMP_SUFFIX);
        // ���̃t�@�C�������l�[�����đޔ������Ă���
        outputFile.renameTo(inputFile);
        
        // �u���o�͊J�n
        replaceOutput(inputFile, outputFile, jdbcUrl, user, password, schema);
        
        // ���̃t�@�C�����폜
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
