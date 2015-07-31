/*
You can easily create multidimensional arrays.
For a multidimensional array of primitives,
you delimit each vector in the array by using curly braces:
 */
package net.miladinov.arrays;
import java.util.*;

public class MultidimensionalPrimitiveArray {
    public static void main(String[] args) {
        int[][] a = {
            {1, 2, 3,},
            {4, 5, 6,}
        };
        System.out.println(Arrays.deepToString(a));
    }
}
