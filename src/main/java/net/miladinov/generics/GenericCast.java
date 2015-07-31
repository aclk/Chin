package net.miladinov.generics;

class FixedSizeStack<T> {
    private int index = 0;
    private Object[] storage;

    public FixedSizeStack(int size) {
        storage = new Object[size];
    }

    public void push(T item) {
        storage[index++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        return (T) storage[--index];
    }
}

public class GenericCast {
    public static final int SIZE = 10;

    public static void main(String[] args) {
        FixedSizeStack<String> strings = new FixedSizeStack<String>(SIZE);

        for (String s : "A B C D E F G H I J".split(" ")) {
            strings.push(s);
        }

        for (int i = 0; i < SIZE; i++) {
            String s = strings.pop();
            System.out.print(s + " ");
        }
    }
}
/*
Without the @SuppressWarnings annotation, the compiler will produce an "unchecked cast" warning for pop().
Because of erasure, it can't know whether the cast is safe, and the pop() method doesn't actually do any casting.
T is erased to its first bound, which is Object by default, so pop() is actually just casting an Object to an Object.
 */
