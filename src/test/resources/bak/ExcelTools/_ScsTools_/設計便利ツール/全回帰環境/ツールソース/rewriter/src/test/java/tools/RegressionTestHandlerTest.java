package tools;


public class RegressionTestHandlerTest { // extends TestCase {

//    @Ignore
//    public void testMain1() throws Exception {
//
//        // 引数作成
//        String[] args =
//            new String[]{"-root", "src/test/resources", "-baseline",
//                         "murata-ps-baseline", "-version",
//                         "[0.5.52-sys-496-c_ps0612-001,0.5.52-sys-496-c_ps0612-999)"};
//        // コンソール実行を模擬
//        RegressionTestHandler.main(args);
//
//    }
//
//    @Ignore
//    public void testMain2() throws Exception {
//
//        // 引数作成
//        String[] args =
//            new String[]{"-root", "src/test/resources",
//                         "-murata-baseline-version", "0.6.0"};
//        // コンソール実行を模擬
//        RegressionTestHandler.main(args);
//
//    }

    public static void main(String[] arg) throws Exception {

        // 引数作成
        String[] args =
            new String[]{
                         "-root",
                         "src/test/resources",
                         "-baseline",
                         "murata-ps-baseline",
                         "-version",
                         "[0.5.52-sys-496-c_ps0612-001,0.5.52-sys-496-c_ps0612-999)",
                         "-murata-baseline-version", "0.6.0"};
        // コンソール実行を模擬
        RegressionTestHandler.main(args);

    }

}
