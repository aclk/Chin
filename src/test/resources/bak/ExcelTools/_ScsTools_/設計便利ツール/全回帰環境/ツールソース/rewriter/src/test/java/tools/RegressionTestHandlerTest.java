package tools;


public class RegressionTestHandlerTest { // extends TestCase {

//    @Ignore
//    public void testMain1() throws Exception {
//
//        // �����쐬
//        String[] args =
//            new String[]{"-root", "src/test/resources", "-baseline",
//                         "murata-ps-baseline", "-version",
//                         "[0.5.52-sys-496-c_ps0612-001,0.5.52-sys-496-c_ps0612-999)"};
//        // �R���\�[�����s��͋[
//        RegressionTestHandler.main(args);
//
//    }
//
//    @Ignore
//    public void testMain2() throws Exception {
//
//        // �����쐬
//        String[] args =
//            new String[]{"-root", "src/test/resources",
//                         "-murata-baseline-version", "0.6.0"};
//        // �R���\�[�����s��͋[
//        RegressionTestHandler.main(args);
//
//    }

    public static void main(String[] arg) throws Exception {

        // �����쐬
        String[] args =
            new String[]{
                         "-root",
                         "src/test/resources",
                         "-baseline",
                         "murata-ps-baseline",
                         "-version",
                         "[0.5.52-sys-496-c_ps0612-001,0.5.52-sys-496-c_ps0612-999)",
                         "-murata-baseline-version", "0.6.0"};
        // �R���\�[�����s��͋[
        RegressionTestHandler.main(args);

    }

}
