package net.miladinov.generics;
import java.util.*;

import net.miladinov.typeinfo.pets.*;

class New {
	public static <K, V> Map<K, V> map() {
		return new HashMap<K, V>();
	}
}

public class ExplicitTypeSpecification {
	static void f(Map<Person, List<Pet>> petPeople) {}

	public static void main(String[] args) {
		f(New.<Person, List<Pet>>map());
	}
}
