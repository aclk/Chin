// Autoboxing compensates for the inability to use primitives in generics.
// However, if performance is a problem, you can use a specialized version
// of the containers adapted for primitive types; one open-source version
// of this is org.apache.commons.collections.primitives
package net.miladinov.generics;
import java.util.*;

public class ListOfInt {
    public static void main(String[] args) {
        List<Integer> li = new ArrayList<Integer>();

        for (int i = 0; i < 5; i++) {
            li.add(i);
        }

        for (int i : li) {
            System.out.print(i + " ");
        }
    }
}
