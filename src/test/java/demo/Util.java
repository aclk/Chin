package demo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Util {

    /**
     * 配列を指定サイズで分割します。分割された配列は List (ArrayList) に格納して戻します。
     * <p>
     * 例)
     * 
     * <pre>
     * String[] array = new Foo[] { "1", "2", "3", "4", "5", "6", "7" };
     * List&lt;String[]&gt; result = Util.splitArray(array, 3, String.class);
     * 
     * ==&gt; [ {"1", "2", "3"}, {"4", "5", "6"}, ("7"} ]
     * </pre>
     * 
     * @param <T>
     *            分割元配列の型
     * @param array
     *            分割元となる配列
     * @param size
     *            分割サイズ
     * @param clazz
     *            分割後配列の型
     * @return 分割された配列のリスト
     */
    public static <T> List<T[]> splitArray(T[] array, int size, Class<?> clazz) {
        List<T[]> list = new ArrayList<T[]>();
        for (int i = 0; i < array.length; i += size) {
            int subLenght = (array.length - i > size) ? size
                    : (array.length - i);
            T[] subArray = newArrayInstance(clazz, subLenght);
            System.arraycopy(array, i, subArray, 0, subLenght);
            list.add(subArray);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] newArrayInstance(Class<?> clazz, int lenght) {
        return (T[]) Array.newInstance(clazz, lenght);
    }
}