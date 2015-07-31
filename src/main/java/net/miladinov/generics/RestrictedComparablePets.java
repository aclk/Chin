package net.miladinov.generics;

class Hamster extends ComparablePet implements Comparable<ComparablePet> {
    @Override
    public int compareTo(ComparablePet o) {
        return super.compareTo(o);
    }
}

// Or just:

class Gecko extends ComparablePet {
    @Override
    public int compareTo(ComparablePet o) {
        return super.compareTo(o);
    }
}

/*
Hamster shows that it is possible to re-implement the same interface that is in
ComparablePet, as long as it is exactly the same, including the parameter types.
However, this is the same as just overriding the methods in the base class,
as seen in Gecko.
*/