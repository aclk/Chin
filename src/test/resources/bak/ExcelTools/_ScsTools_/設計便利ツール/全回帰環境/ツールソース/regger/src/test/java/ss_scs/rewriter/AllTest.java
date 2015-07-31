package ss_scs.rewriter;

import org.junit.Test;
/**
 * ƒeƒXƒg
 *
 * @author A1B5MEMO
 */
public class AllTest {

    @Test
    public void testAll() {
        ReWriterXai.main(new String[] {"-t"  , "D:/workspace/murata-ss-bsp" , "-p" , "autoTestInterceptor" , "-d" , "src/test/resources/applicationContext-datasource-local.xml" , "-j" , "src/test/resources/jdbc.properties"});
    }

}