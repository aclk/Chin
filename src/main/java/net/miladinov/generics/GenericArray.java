package net.miladinov.generics;

import java.util.Arrays;

public class GenericArray<T> {
    private T[] array;

    public GenericArray(int size) {
        array = (T[]) new Object[size];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index, T item) {
        return array[index];
    }

    // Method that exposes the underlying representation
    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArray<Integer> gai = new GenericArray<Integer>(10);
        // This causes a ClassCastException
        Integer[] ia = gai.rep();
        System.out.println(Arrays.toString(ia));
        /// This is OK:
        Object[] oa = gai.rep();
        System.out.println(Arrays.toString(oa));
    }

}
