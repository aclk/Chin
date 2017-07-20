package net.miladinov.initialization;

public class OptionalTrailingArguments {
	static void f(int required, String... trailing) {
		System.out.print("required: " + required + " ");
		for (String s : trailing) {
			System.out.print(s + " ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		f(1, "one");
		f(2, "two", "three");
		f(0);
		System.out.println("int[]: " + new int[0].getClass());
	}	
}