// Building up a complex model using generic containers.
package net.miladinov.generics;

import net.miladinov.generics.coffee.Generators;
import net.miladinov.util.Generator;

import java.util.ArrayList;
import java.util.Random;

class Product {
    private final int id;
    private String description;
    private double price;

    public Product(int id, String description, double price) {
        this.id = id;
        this.description = description;
        this.price = price;
        System.out.println(toString());
    }

    public String toString() {
        return id + ": " + description + ", price: $" + price;
    }

    public void priceChange(double change) {
        price += change;
    }

    public static Generator<Product> generator =
        new Generator<Product>() {
            private Random rand = new Random(47);
            public Product next() {
                return new Product(rand.nextInt(1000), "Test", Math.round(rand.nextDouble() * 1000.0) + 0.99);
            }
        };
}

class Shelf extends ArrayList<Product> {
    public Shelf(int numProducts) {
        Generators.fill(this, Product.generator, numProducts);
    }
}

class Aisle extends ArrayList<Shelf> {
    public Aisle(int numShelves, int numProducts) {
        for (int i = 0; i < numShelves; i++) {
            add(new Shelf(numProducts));
        }
    }
}

class CheckoutStand {}
class Office {}

public class Store extends ArrayList<Aisle> {
    private ArrayList<CheckoutStand> checkouts = new ArrayList<CheckoutStand>();
    private Office office = new Office();

    public Store(int numAisles, int numShelves, int numProducts) {
        for (int i = 0; i < numAisles; i++) {
            add(new Aisle(numShelves, numProducts));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Aisle a: this) {
            for (Shelf s : a) {
                for (Product p : s) {
                    result.append(p);
                    result.append("\n");
                }
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Store(14, 5, 10));
    }
}
