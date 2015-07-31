// A Map Filled with data using a generator object.
package net.miladinov.util;

import java.util.*;

public class MapData<K, V> extends LinkedHashMap<K, V> {
    // A single Pair Generator:
    public MapData(Generator<Pair<K, V>> generator, int quantity) {
        for (int i = 0; i < quantity; i++) {
            Pair<K, V> p = generator.next();
            put(p.key, p.value);
        }
    }

    // Two separate Generators:
    public MapData(Generator<K> keyGenerator, Generator<V> valueGenerator, int quantity) {
        for (int i = 0; i < quantity; i++) {
            put(keyGenerator.next(), valueGenerator.next());
        }
    }

    // A key Generator and a single value:
    public MapData(Generator<K> keyGenerator, V value, int quantity) {
        for (int i = 0; i < quantity; i++) {
            put(keyGenerator.next(), value);
        }
    }

    // An Iterable and a value Generator:
    public MapData(Iterable<K> keyIterator, Generator<V> valueGenerator) {
        for (K key : keyIterator) {
            put(key, valueGenerator.next());
        }

    }

    // An Iterable and a single value:
    public MapData(Iterable<K> keyIterator, V value) {
        for (K key : keyIterator) {
            put(key, value);
        }
    }

    // Generic convenience methods:
    public static <K, V> MapData<K, V> map(Generator<Pair<K, V>> generator, int quantity) {
        return new MapData<K, V>(generator, quantity);
    }

    public static <K, V> MapData<K, V> map(Generator<K> keyGenerator, Generator<V> valueGenerator, int quantity) {
        return new MapData<K, V>(keyGenerator, valueGenerator, quantity);
    }

    public static <K, V> MapData<K, V> map(Generator<K> keyGenerator, V value, int quantity) {
        return new MapData<K, V>(keyGenerator, value, quantity);
    }

    public static <K, V> MapData<K, V> map(Iterable<K> keyIterator, Generator<V> valueGenerator) {
        return new MapData<K, V>(keyIterator, valueGenerator);
    }

    public static <K, V> MapData<K, V> map(Iterable<K> keyIterator, V value) {
        return new MapData<K, V>(keyIterator, value);
    }
}
