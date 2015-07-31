package net.miladinov.generics;

import java.lang.reflect.*;
import java.util.*;

/*
For new code, you should pass in a type token. In that case, the GenericArray looks like this:
*/
public class GenericArrayWithTypeToken<T> {
    private T[] array;

    @SuppressWarnings("unchecked")
    public GenericArrayWithTypeToken(Class<T> type, int size) {
        array = (T[]) Array.newInstance(type, size);
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }
    
    // Expose the underlying representation
    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<Integer>(Integer.class, 10);
        // This now works:
        Integer[] ia = gai.rep();
        System.out.println(Arrays.toString(ia));
    }
}
