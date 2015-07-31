package net.miladinov.generics;

import java.math.*;
import java.util.concurrent.atomic.*;
import java.util.*;

import static net.miladinov.util.Print.*;

// Different types of function objects:
interface Combiner<T> {
    T combine(T x, T y);
}

interface UnaryFunction<R, T> {
    R function(T x);
}

// Extract result of collecting parameter
interface Collector<T> extends UnaryFunction<T, T> {
    T result();
}

interface UnaryPredicate<T> {
    boolean test(T x);
}

public class Functional {
    // Calls the Combiner object on each element to combine
    // it with a running result, which is finally returned:
    public static <T> T reduce(Iterable<T> sequence, Combiner<T> combiner) {
        Iterator<T> it = sequence.iterator();

        if (it.hasNext()) {
            T result = it.next();
            while (it.hasNext()) {
                result = combiner.combine(result, it.next());
            }
            return result;
        }

        // If sequence is the empty list:
        return null; // Or throw exception
    }

    // Take a function object and call it on each object in the list,
    // ignoring the return value.  Then function object may act as a collecting parameter,
    // so it is returned at the end.
    public static <T> Collector<T> forEach(Iterable<T> sequence, Collector<T> func) {
        for (T t : sequence) {
            func.function(t);
        }
        return func;
    }

    // Creates a list of results by calling a function object for each object in the list:
    public static <R, T> List<R> transform(Iterable<T> sequence, UnaryFunction<R, T> func) {
        List<R> result = new ArrayList<R>();
        for (T t : sequence) {
            result.add(func.function(t));
        }
        return result;
    }

    // Applies a unary predicate to each item in a sequence,
    // and returns a list of items that produced "true":
    public static <T> List<T> filter(Iterable<T> sequence, UnaryPredicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T t : sequence) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    // To use the above generic methods, we need to create function object to adapt to our particular needs:
    static class IntegerAdder implements Combiner<Integer> {
        public Integer combine(Integer x, Integer y) {
            return x + y;
        }
    }

    static class IntegerSubtracter implements Combiner<Integer> {
        public Integer combine(Integer x, Integer y) {
            return x - y;
        }
    }

    static class BigDecimalAdder implements Combiner<BigDecimal> {
        public BigDecimal combine(BigDecimal x, BigDecimal y) {
            return x.add(y);
        }
    }

    static class BigIntegerAdder implements Combiner<BigInteger> {
        public BigInteger combine(BigInteger x, BigInteger y) {
            return x.add(y);
        }
    }

    static class AtomicLongAdder implements Combiner<AtomicLong> {
        public AtomicLong combine(AtomicLong x, AtomicLong y) {
            // Not clear whether this is meaningful:
            return new AtomicLong(x.addAndGet(y.get()));
        }
    }

    // We can even make a UnaryFunction with an "ulp" (Units in the last place):
    static class BigDecimalUlp implements UnaryFunction<BigDecimal, BigDecimal> {
        public BigDecimal function(BigDecimal x) {
            return x.ulp();
        }
    }

    static class GreaterThan<T extends Comparable<T>> implements UnaryPredicate<T> {
        private T bound;

        public GreaterThan(T bound) {
            this.bound = bound;
        }

        public boolean test(T x) {
            return x.compareTo(bound) > 0;
        }
    }

    static class MultiplyingIntegerCollector implements Collector<Integer> {
        private Integer val = 1;

        public Integer function(Integer x) {
            val *= x;
            return val;
        }

        public Integer result() {
            return val;
        }
    }

    public static void main(String[] args) {
        // Generics, varargs & boxing working together:
        List<Integer> li = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Integer result = reduce(li, new IntegerAdder());
        print(result);

        result = reduce(li, new IntegerSubtracter());
        print(result);

        print(filter(li, new GreaterThan<Integer>(4)));

        print(forEach(li, new MultiplyingIntegerCollector()).result());

        print(forEach(filter(li, new GreaterThan<Integer>(4)), new MultiplyingIntegerCollector()).result());

        MathContext mc = new MathContext(7);
        List<BigDecimal> lbd = Arrays.asList(
            new BigDecimal(1.1, mc), new BigDecimal(2.2, mc),
            new BigDecimal(3.3, mc), new BigDecimal(4.4, mc)
        );
        BigDecimal rbd = reduce(lbd, new BigDecimalAdder());
        print(rbd);

        print(filter(lbd, new GreaterThan<BigDecimal>(new BigDecimal(3))));

        // Use the prime-generation facility of BigInteger:
        List<BigInteger> lbi = new ArrayList<BigInteger>();
        BigInteger bi = BigInteger.valueOf(11);
        for (int i = 0; i < 11; i++) {
            lbi.add(bi);
            bi = bi.nextProbablePrime();
        }
        print(lbi);

        BigInteger rbi = reduce(lbi, new BigIntegerAdder());
        print(rbi);

        // The sum of this list of primes is also prime:
        print(rbi.isProbablePrime(5));

        List<AtomicLong> lal = Arrays.asList(
            new AtomicLong(11), new AtomicLong(47),
            new AtomicLong(74), new AtomicLong(133)
        );
        AtomicLong ral = reduce(lal, new AtomicLongAdder());
        print(ral);

        print(transform(lbd, new BigDecimalUlp()));
    }
}
