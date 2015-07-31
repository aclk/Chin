package net.miladinov.generics;
import java.util.*;

public class GenericsAndCovariance {
    public static void main(String[] args) {
        // Wildcards allow covariance:
        List<? extends Fruit> fruitList = new ArrayList<Apple>();
        // Compile Error: can't add any type of object
        // fruitList.add(new Apple());
        // fruitList.add(new Fruit());
        // fruitList.add(new Object());
        fruitList.add(null); // Legal but uninteresting
        // We know that it returns at least Fruit:
        Fruit f = fruitList.get(0);
    }
}
