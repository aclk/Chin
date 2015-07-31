package net.miladinov.holding;
import net.miladinov.typeinfo.pets.*;

import java.util.*;

public class CollectionSequence implements Collection {
	private List<Pet> pets = Arrays.asList(Pets.createArray(8));

	
	public Iterator<Pet> iterator() {
		return new Iterator<Pet>() {
			private int index = 0;
			
			public boolean hasNext() {
				return index < size();
			}

			
			public Pet next() {
				return pets.get(index++);
			}

			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	
	public int size() {
		return pets.size();
	}

	
	public boolean add(Object e) {
		return pets.add((Pet)e);
	}

	
	public boolean addAll(Collection c) {
		return pets.addAll(c);
	}

	
	public void clear() {
		pets.clear();
	}

	
	public boolean contains(Object o) {
		return pets.contains(o);
	}

	
	public boolean containsAll(Collection c) {
		return pets.containsAll(c);
	}

	
	public boolean isEmpty() {
		return pets.isEmpty();
	}

	
	public boolean remove(Object o) {
		return pets.remove(o);
	}

	
	public boolean removeAll(Collection c) {
		return pets.removeAll(c);
	}

	
	public boolean retainAll(Collection c) {
		return pets.retainAll(c);
	}

	
	public Object[] toArray() {
		return pets.toArray();
	}

	
	public Object[] toArray(Object[] a) {
		return pets.toArray(a);
	}

	public static void main(String[] args) {
		CollectionSequence c = new CollectionSequence();
		InterfaceVsIterator.display(c);
		InterfaceVsIterator.display(c.iterator());
	}
}