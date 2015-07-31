package ss_scs.rewriter;

import java.io.File;
import java.util.regex.Pattern;

/**
 * �f�B���N�g���������s��
 * @author A1B5MEMO
 *
 */
public class DirectoryWalker {

    /**
     * �ċA�Ăяo���Ńf�B���N�g���c���[������܂�
     * @param target �Ώۃt�@�C���i�f�B���N�g���܂ށj
     * @param targetFilepattern �Ώۃt�@�C���p�^�[��(���K�\��)
     * @param ignorePattern �ΏۊO�t�@�C���p�^�[��
     * @param command �t�@�C�������R�}���h�̃R�[���o�b�N
     */
    public void walkAround(File target, String targetFilepattern,
        IgnorePatterns ignorePattern, FileRewriteCommand command) {

        if (ignorePattern != null && ignorePattern.isIgnore(target.getName())) {
            // ignore ���߂ɖ�������("target", ".svn"�Ȃǂ�z��)
            return;
        }
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            for (File f : files) {
                // �ċA�Ăяo���I
                this.walkAround(f, targetFilepattern, ignorePattern, command);
            }
        } else if (target.isFile()) {
            // �召�����𖳎����ă}�b�`���邩��r
            Pattern p = Pattern.compile(targetFilepattern, Pattern.CASE_INSENSITIVE);
            if (p.matcher(target.getName()).matches()) {
                command.rewrite(target);
            }
        }
    }
}
