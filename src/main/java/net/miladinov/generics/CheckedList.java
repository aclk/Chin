// Using Collection.checkedList().
package net.miladinov.generics;
import net.miladinov.typeinfo.pets.*;
import java.util.*;

public class CheckedList {
    @SuppressWarnings("unchecked")
    static void oldStyledMethod(List probablyDogs) {
        probablyDogs.add(new Cat());
    }

    public static void main(String[] args) {
        List<Dog> dogs1 = new ArrayList<Dog>();
        oldStyledMethod(dogs1); // Quietly accepts a Cat

        List<Dog> dogs2 = Collections.checkedList(new ArrayList<Dog>(), Dog.class);

        try {
            oldStyledMethod(dogs2); // Throws an exception
        } catch (Exception e) {
            System.out.println(e);
        }

        // Derived types work fine:
        List<Pet> pets = Collections.checkedList(new ArrayList<Pet>(), Pet.class);
        pets.add(new Dog());
        pets.add(new Cat());
    }
}
