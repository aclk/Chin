package ss_scs.rewriter;

/**
 * 無視するパターンを保持し、判定を行うオブジェクトが実装すべきインターフェース
 * @author A1B5MEMO
 */
public interface IgnorePatterns {
    /**
     * シカト判定
     * @param absolutePath
     * @return
     */
    boolean isIgnore(String path);
}
