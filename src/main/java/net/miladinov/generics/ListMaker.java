package net.miladinov.generics;
import java.util.*;

public class ListMaker<T> {
    List<T> create() { return new ArrayList<T>(); }

    public static void main(String[] args) {
        ListMaker<String> stringListMaker = new ListMaker<String>();
        List<String> stringList = stringListMaker.create();
    }
}
