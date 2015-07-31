package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestCollectionsSort {
    public static void main(String[] args) {
        List<String> strList4sort = new ArrayList<String>();

        strList4sort.add("Car");
        strList4sort.add("Air");
        strList4sort.add("Box");
        strList4sort.add("Do");

        // ソート（デフォルト：昇順）
        // Collections.sort(strList4sort); これもOK
        Collections.sort(strList4sort, new StringComparator());

        // ソート後の値
        System.out.println("*************ソート後の値：***************");
        for (String value : strList4sort) {
            System.out.println(value);
        }

        // ソート（降順）
        Collections.sort(strList4sort, new StringComparator(
                StringComparator.DESC));

        // ソート後の値
        System.out.println("*************ソート後の値：***************");
        for (String value : strList4sort) {
            System.out.println(value);
        }
    }
}

// Comparator実装クラス
class StringComparator implements Comparator {
    public static final int ASC = 1; // 昇順
    public static final int DESC = -1; // 降順
    private int sort = ASC; // デフォルトは昇順

    public StringComparator() {

    }

    /**
     * @param sort
     *            StringComparator.ASC | StringComparator.DESC。昇順や降順を指定します。
     */
    public StringComparator(int sort) {
        this.sort = sort;
    }

    public int compare(Object arg0, Object arg1) {
        if (!(arg0 instanceof Comparable) || !(arg1 instanceof Comparable)) {
            throw new IllegalArgumentException(
                    "arg0 & arg1 must implements interface of java.lang.Comparable.");
        }
        if (arg0 == null && arg1 == null) {
            return 0; // arg0 = arg1
        } else if (arg0 == null) {
            return 1 * sort; // arg1 > arg2
        } else if (arg1 == null) {
            return -1 * sort; // arg1 < arg2
        }

        return ((Comparable) arg0).compareTo((Comparable) arg1) * sort;
    }

}