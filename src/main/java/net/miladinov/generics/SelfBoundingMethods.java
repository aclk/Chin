package net.miladinov.generics;

// It's also possible to use self-bounding for generic methods:
public class SelfBoundingMethods {
    static <T extends SelfBounded<T>> T f(T arg) {
        return arg.set(arg).get();
    }

    public static void main(String[] args) {
        A a = f(new A());
    }
}
// This prevents the method from being applied to anything but a self-bounded argument of the form shown.
