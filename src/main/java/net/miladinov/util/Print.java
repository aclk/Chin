package net.miladinov.util;
import java.io.PrintStream;

public class Print {

	public static void print(Object obj) {
		System.err.println(obj);
	}
	
	public static void print() {
		System.err.println();
	}
	
	public static void printnb(Object obj) {
		System.err.print(obj);
	}
	
	public static PrintStream printf(String format, Object... args) {
		return System.err.printf(format, args);
	}	
}