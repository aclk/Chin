/*
In general, arrays and generics do not mix well. You cannot instantiate arrays of parameterized types:

Peel<Banana>[] peels = new Peel<Banana>[10]; // Illegal

Erasure removes the parameter type information, and arrays must know
the exact type that they hold, in order to enforce type safety.

However, you can parameterized the type of the array itself:
*/
package net.miladinov.arrays;

class ClassParameter<T> {
    public T[] f(T[] arg) {
        return arg;
    }
}

class MethodParameter {
    public static <T> T[] f(T[] arg) {
        return arg;
    }
}

public class ParameterizedArrayType {
    public static void main(String[] args) {
        Integer[] integers = {1, 2, 3, 4, 5,};
        Double[] doubles = {1.1, 2.2, 3.3, 4.4, 5.5,};
        Integer[] integers2 = new ClassParameter<Integer>().f(integers);
        Double[] doubles2 = new ClassParameter<Double>().f(doubles);
        integers2 = MethodParameter.f(integers);
        doubles2 = MethodParameter.f(doubles);
    }
}
