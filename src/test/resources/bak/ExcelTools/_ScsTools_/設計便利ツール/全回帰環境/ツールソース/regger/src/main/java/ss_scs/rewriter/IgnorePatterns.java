package ss_scs.rewriter;

/**
 * ��������p�^�[����ێ����A������s���I�u�W�F�N�g���������ׂ��C���^�[�t�F�[�X
 * @author A1B5MEMO
 */
public interface IgnorePatterns {
    /**
     * �V�J�g����
     * @param absolutePath
     * @return
     */
    boolean isIgnore(String path);
}
