package net.miladinov.arrays;

import java.util.Arrays;

public class ThreeDWithNew {
    public static void main(String[] args) {
        // 3-D array with fixed length:
        int[][][] a = new int[4][3][2];
        System.out.println(Arrays.deepToString(a));
    }
}
