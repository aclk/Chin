package ss_scs.rewriter;

import java.io.File;

/**
 * ファイル編集コマンド
 * @author A1B5MEMO
 */
public interface FileRewriteCommand {
    void rewrite(File targetFile);
}
