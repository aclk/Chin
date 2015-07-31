package ss_scs.rewriter;

import java.io.File;
import java.util.regex.Pattern;

/**
 * ディレクトリ走査を行う
 * @author A1B5MEMO
 *
 */
public class DirectoryWalker {

    /**
     * 再帰呼び出しでディレクトリツリーを歩きます
     * @param target 対象ファイル（ディレクトリ含む）
     * @param targetFilepattern 対象ファイルパターン(正規表現)
     * @param ignorePattern 対象外ファイルパターン
     * @param command ファイル走査コマンドのコールバック
     */
    public void walkAround(File target, String targetFilepattern,
        IgnorePatterns ignorePattern, FileRewriteCommand command) {

        if (ignorePattern != null && ignorePattern.isIgnore(target.getName())) {
            // ignore 早めに無視する("target", ".svn"などを想定)
            return;
        }
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            for (File f : files) {
                // 再帰呼び出し！
                this.walkAround(f, targetFilepattern, ignorePattern, command);
            }
        } else if (target.isFile()) {
            // 大小文字を無視してマッチするか比較
            Pattern p = Pattern.compile(targetFilepattern, Pattern.CASE_INSENSITIVE);
            if (p.matcher(target.getName()).matches()) {
                command.rewrite(target);
            }
        }
    }
}
