package test.annotation;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Formatter;

import org.junit.Test;

public class AnnotationTest {

    // @Test
    // public void should_test_utils() throws InvocationTargetException,
    // IllegalAccessException {
    // Home home = new Home();
    // assertEquals("andy", home.getName());
    // assertEquals(7, home.getCount());
    // }

    // 格式化说明符的使用例子
    private Formatter f = new Formatter(System.out);

    public void printHead() {
        f.format("%-10s %5s %5s%n", "username", "level", "score");
        f.format("%-10s %5s %5s%n", "----------", "-----", "-----");
    }

    public void printData() {
        f.format("%-10s %5d %5.2f%n", "Jason", 1, 9.87654321);
        f.format("%-10s %5d %5.2f%n", "arthinking", 2, 9.6512);
    }

    @Test
    public void testMain() {
        printHead();
        printData();
    }
}
