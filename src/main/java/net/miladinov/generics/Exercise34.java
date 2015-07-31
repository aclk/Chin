package net.miladinov.generics;

abstract class Flouter<T extends Flouter<T>> {
    abstract T flout(Flouter f);

    T getFlouter() {
        return flout(this);
    }
}

class ConcreteFlouter extends Flouter {

    Flouter flout(Flouter f) {
        return this;
    }
}

public class Exercise34 {
    public static void main(String[] args) {
        System.out.println("Create a self-bounded generic type that contains an abstract" +
            "method that takes an argument of the generic type parameter and produces " +
            "a return value of the generic type parameter. In a non-abstract method of the class," +
            "call the abstract method and return its result. Inherit from the self-bounded type" +
            "and test the resulting class.");

        ConcreteFlouter cf = new ConcreteFlouter();
        System.out.println(cf.getFlouter().getClass().getSimpleName());
    }
}
