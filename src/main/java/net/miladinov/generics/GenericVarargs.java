package net.miladinov.generics;
import java.util.*;

public class GenericVarargs {
	
	public static <T> List<T> makeList(T... args) {
		List<T> result = new ArrayList<T>();
		for (T item : args) {
			result.add(item);
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(makeList("A"));
		System.out.println(makeList("A", "B", "C"));
		System.out.println(makeList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("")));
	}
}