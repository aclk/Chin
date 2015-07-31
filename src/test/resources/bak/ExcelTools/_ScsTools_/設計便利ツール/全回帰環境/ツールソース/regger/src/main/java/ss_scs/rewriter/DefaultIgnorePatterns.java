package ss_scs.rewriter;

import java.util.HashSet;
import java.util.Set;

/**
 * ��������p�^�[����ێ����A������s��
 * "target", ".svn"�𖳎�
 * @author A1B5MEMO
 */
public class DefaultIgnorePatterns implements IgnorePatterns {

    /** ��������p�^�[���̃R���N�V���� */
    private Set<String> collection = new HashSet<String>();

    /**
     * constructor
     */
    public DefaultIgnorePatterns() {
        this.init();
    }

    /**
     * ������
     * �p�^�[����������Ԃɖ߂��܂�
     */
    public void init() {
        this.add("target");
        this.add("\\.svn");
    }

    /**
     * �p�^�[�����Z�b�g
     */
    public void reset() {
        this.collection.clear();
    }

    /**
     * �p�^�[����ǉ�����
     * @param pattern
     */
    public void add(String pattern) {
        this.collection.add(pattern);
    }

    /**
     * �V�J�g����
     * @param absolutePath
     * @return
     */
    public boolean isIgnore(String path) {
        for (String ignorePattern : this.collection) {
            if (path.matches(ignorePattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.collection.toString();
    }
}
