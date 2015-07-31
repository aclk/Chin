package ss_scs.rewriter;

import java.util.HashSet;
import java.util.Set;

/**
 * 無視するパターンを保持し、判定を行う
 * "target", ".svn"を無視
 * @author A1B5MEMO
 */
public class DefaultIgnorePatterns implements IgnorePatterns {

    /** 無視するパターンのコレクション */
    private Set<String> collection = new HashSet<String>();

    /**
     * constructor
     */
    public DefaultIgnorePatterns() {
        this.init();
    }

    /**
     * 初期化
     * パターンを初期状態に戻します
     */
    public void init() {
        this.add("target");
        this.add("\\.svn");
    }

    /**
     * パターンリセット
     */
    public void reset() {
        this.collection.clear();
    }

    /**
     * パターンを追加する
     * @param pattern
     */
    public void add(String pattern) {
        this.collection.add(pattern);
    }

    /**
     * シカト判定
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
