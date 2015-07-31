package net.miladinov.holding;
import java.util.*;

public class SortedSetOfInteger {
	public static void main(String[] args) {
		Random rand = new Random(47);
		SortedSet<Integer> intSet = new TreeSet<Integer>();
		
		for(int i = 0; i < 10000; i++) {
			intSet.add(rand.nextInt(30));
		}
		
		System.out.println(intSet);
	}
}
