package net.miladinov.strings;
// Allows you to easily try out regular expressions.
// {Args: abcabcabcdefabc "abc+" "(abc)+" "(abc){2,}" }
import java.util.regex.*;
import static net.miladinov.util.Print.*;

public class TestRegularExpression {
	public static void main(String[] args) {
		if (args.length < 2) {
			print("Usage:\njava TestRegularExpression characterSequence regularExpression+");
			System.exit(0);
		}
		
		String characterSequence = args[0];
		print("Input: \"" + characterSequence + "\"");
		
		for (String arg : args) {
			print("Regular expression: \"" + arg + "\"");
			Pattern p = Pattern.compile(arg);
			Matcher m = p.matcher(characterSequence);
			
			while (m.find()) {
				print("Match \"" + m.group() + "\" at positions " + m.start() + "-" + (m.end() - 1));
			}
		}
	}
}