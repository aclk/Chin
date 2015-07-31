package net.miladinov.fibonacci;

import java.util.Arrays;

public class Fibonacci {

    public int[] getFibonacciSequence(int numberOfFibonaccis) {
        int[] fibonacciArray = new int[numberOfFibonaccis];
        int position = 0;

        do {
            fibonacciArray[position] = ((position > 1) ? (fibonacciArray[position - 2] + fibonacciArray[position - 1])
                    : 1);
            position++;
        } while (position < numberOfFibonaccis);

        return fibonacciArray;
    }

    public static void main(String[] args) {
        Fibonacci f = new Fibonacci();
        System.out.println(Arrays.toString(f.getFibonacciSequence(50)));
    }
}