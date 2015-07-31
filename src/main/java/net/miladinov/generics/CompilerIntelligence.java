package net.miladinov.generics;
import java.util.*;

public class CompilerIntelligence {
    public static void main(String[] args) {
        List<? extends Fruit> fruitList = Arrays.asList(new Apple());
        Apple a = (Apple) fruitList.get(0); // No warning
        fruitList.contains(new Apple()); // Argument is 'Object'
        fruitList.indexOf(new Apple()); // Argument is 'Object'
    }
}
