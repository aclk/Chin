package net.miladinov.strings;
import java.util.regex.*;
import static net.miladinov.util.Print.*;

/**
 * The pattern '\\w=' splits the input into words. find() is like an iterator, moving forward through the input string. 
 * However, the second version of find() can be given an integer argument that tells it the character position for 
 * the beginning of the search -- this version resets the search position to the value of the argument, as you can see
 * from the output.
 */
public class Finding {
	public static void main(String[] args) {
		Matcher m = Pattern.compile("\\w+").matcher("Evening is full of the linnet's wings");
		
		while (m.find()) {
			printnb(m.group() + " ");
		}
		
		print();
		
		int i = 0;
		
		while (m.find(i++)) {
			printnb(m.group() + " ");
		}
	}
}
