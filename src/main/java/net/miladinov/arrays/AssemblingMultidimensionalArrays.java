// Here's how an array of non-primitive object can be built up piece-by-piece:
package net.miladinov.arrays;
import java.util.*;

public class AssemblingMultidimensionalArrays {
    public static void main(String[] args) {
        Integer[][] a;
        a = new Integer[3][];

        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer[3];
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = i * j; // Autoboxing
            }
        }
        System.out.println(Arrays.deepToString(a));
    }
}
// The i*j is only there to put an interesting value into the Integer.
