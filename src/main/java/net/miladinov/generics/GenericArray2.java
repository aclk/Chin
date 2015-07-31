package net.miladinov.generics;
/*
Because of erasure, the runtime type of the array can only be Object[]. If we immediately cast it to T[], 
then at compile time the actual type of the array is lost, band the compiler may miss out on some potential 
error checks. Because of this, it's better to use an Object[] inside the collection, and add a cast to T 
when you use an array element. Let's see how that would look with the GenericArray.java example:
 */

public class GenericArray2<T> {
    private Object[] array;

    public GenericArray2(int size) {
        array = new Object[size];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return (T) array[index];
    }
    
    public T[] rep() {
        return (T[]) array;
    }

    public static void main(String[] args) {
        GenericArray2<Integer> gai = new GenericArray2<Integer>(10);
        for (int i = 0; i < 10; i++) {
            gai.put(i, i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(gai.get(i) + " ");
        }
        System.out.println();
        
        try {
            Integer[] ia = gai.rep();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
