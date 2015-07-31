package ss_scs.rewriter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
/**
 * ƒeƒXƒg
 *
 * @author A1B5MEMO
 */
public class ReWriterXaiTest {

    @Test
    public void testDbConnectSetting() {
        DbConnectSetting dbc = new DbConnectSetting();
        assertTrue( dbc.load("src/test/resources/applicationContext-datasource-local.xml"));
    }

}