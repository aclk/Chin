package net.miladinov.holding;
import java.util.*;

public class SetOfInteger {
	public static void main(String[] args) {
		Random rand = new Random(47);
		Set<Integer> intSet = new HashSet<Integer>();
		
		for (int i = 0; i < 1000; i++) {
			intSet.add(rand.nextInt(30));
		}
		
		System.out.println(intSet);
	}
}