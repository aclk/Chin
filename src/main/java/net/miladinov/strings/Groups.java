package net.miladinov.strings;
import java.util.regex.*;
import static net.miladinov.util.Print.*;

public class Groups {
	static public final String POEM = 
		"Twas brilling, and the slithy toves\n" +
		"Did gyre and gimble in the wabe.\n" +
		"All mimsy were the borogroves,\n" +
		"And the mome raths outgrabe.\n\n" +
		"Beware the Jabberwock, my son,\n" +
		"The jaws that bite, the claws that catch.\n" +
		"Beware the JubJub bird, and shun\n" +
		"The frumious Bandersnatch.";
	
	public static void main(String[] args) {
		Matcher m = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$").matcher(POEM);
		
		while (m.find()) {
			for (int j = 0; j <= m.groupCount(); j++) {
				printnb("[" + m.group(j) + "]");
			}
			print();
		}
	}	
}