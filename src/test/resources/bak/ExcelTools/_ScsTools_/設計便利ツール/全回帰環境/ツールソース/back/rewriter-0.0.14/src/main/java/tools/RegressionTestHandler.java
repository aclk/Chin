package tools;

import tools.directorywalker.BeanXMLReWriter;
import tools.directorywalker.POMXMLReWriter;
import tools.directorywalker.TestXMLForAutotestReWriter;
import tools.directorywalker.TestXMLReWriter;

public class RegressionTestHandler {

    static final String DEFAULT_SCHEMA = "ss";

    static final String DEFAULT_BASE_LINE = "murata-ps-baseline";

    static final String DEFAULT_BASE_LINE_VERSION = "(,0.5.52-sys-999)";

    static final boolean $assertionsDisabled = true;

    public RegressionTestHandler() {
    }

    public static void main(String args[]) throws Exception {
        if (!$assertionsDisabled && args.length != 6 && args.length != 2)
            throw new AssertionError();
        String root = null;
        String schema = null;
        String baseline = null;
        String version = null;
        boolean isAutotest = false;
        for (int i = 0; i < args.length; i++) {
            if ("-root".equals(args[i])) {
                root = args[i + 1];
                if (!root.endsWith("\\")) root = root.concat("\\");
                continue;
            }
            if ("-schema".equals(args[i])) {
                schema = args[i + 1];
                continue;
            }
            if ("-baseline".equals(args[i])) {
                baseline = args[i + 1];
                continue;
            }
            if ("-version".equals(args[i])) {
                version = args[i + 1];
                continue;
            }
            if ("-autotest".equals(args[i])) {
                isAutotest= true;
                continue;
            }
        }

        System.out.println("-----テスト設定ファイルチェック＆書換-----");
        (new TestXMLReWriter(schema == null ? DEFAULT_SCHEMA : schema)).walk(root);
        System.out.println("-----pom.xml チェック＆書換-----");
        (new POMXMLReWriter(baseline == null ? "murata-ss-sa-baseline": baseline, version == null ? DEFAULT_BASE_LINE_VERSION : version)).walk(root);
        System.out.println("-----bean.xml チェック＆書換-----");
        (new BeanXMLReWriter()).walk(root);
        if (isAutotest) {
            System.out.println("-----テスト設定ファイルのエビデンス出力用設定-----");
            (new TestXMLForAutotestReWriter()).walk(root);
        }
    }

}
