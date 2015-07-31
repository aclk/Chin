package net.miladinov.holding;
import java.util.*;

public class MultiIterableClass extends IterableClass {

	public Iterable<String> reversed() {
		return new Iterable<String>() {
			public Iterator<String> iterator() {
				return new Iterator<String>() {
					int current = words.length - 1;
					
					
					public boolean hasNext() { return current > -1; }
					
					
					public String next() { return words[current--]; }

					
					public void remove() { // Not implemented 
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
		
	public Iterable<String> randomized() {
		return new Iterable<String>() {
			
			public Iterator<String> iterator() {
				List<String> shuffled = new ArrayList<String>(Arrays.asList(words));
				Collections.shuffle(shuffled, new Random(47));
				return shuffled.iterator();
			}
		};
	}
	
	public static void main(String[] args) {
		MultiIterableClass mic = new MultiIterableClass();
		printIterable(mic.reversed());
		printIterable(mic.randomized());
		printIterable(mic);
	}

	private static void printIterable(Iterable<String> iterator) {
		for (String s : iterator) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
}