package demo;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UtilTest {

    @Test
    public void splitArrayのテスト_配列が分割されるパターン() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 8; i++) {
            list.add(String.valueOf(i));
        }
        String[] array = list.toArray(new String[] {});

        List<String[]> actual = Util.splitArray(array, 3, String.class);

        assertThat(actual.size(), is(3));

        assertThat(actual.get(0).length, is(3));
        assertThat(actual.get(0), is(new String[] { "0", "1", "2" }));

        assertThat(actual.get(1).length, is(3));
        assertThat(actual.get(1), is(new String[] { "3", "4", "5" }));

        assertThat(actual.get(2).length, is(2));
        assertThat(actual.get(2), is(new String[] { "6", "7" }));
    }

    @Test
    public void splitArrayのテスト_配列が分割されないパターン() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add(String.valueOf(i));
        }
        String[] array = list.toArray(new String[] {});

        List<String[]> actual = Util.splitArray(array, 5, String.class);

        assertThat(actual.size(), is(1));
        assertThat(actual.get(0), is(new String[] { "0", "1", "2", "3", "4" }));
    }

    @Test
    public void splitArrayのテスト_配列が巨大な場合のパフォーマンス確認() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 1000000; i++) {
            list.add(String.valueOf(i));
        }
        String[] array = list.toArray(new String[] {});

        final int splitSize = 10000;

        long start = System.nanoTime();
        List<String[]> actual = Util.splitArray(array, splitSize, String.class);
        long execTime = System.nanoTime() - start;
        System.out.println(execTime / 1000 / 1000 + " msec.");

        assertThat(actual.size(), is(100));
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i).length, is(splitSize));
        }
    }
}