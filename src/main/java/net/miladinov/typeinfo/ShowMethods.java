package net.miladinov.typeinfo;
import java.lang.reflect.*;
import java.util.regex.*;
import static net.miladinov.util.Print.*;

public class ShowMethods {
	
	private static String usage = "usage\n" +
			"ShowMethods qualified class.name\n" +
			"To show all methods in class or:\n" +
			"ShowMethods qualified.class.name word\n" +
			"To search for methods involvig 'word'";
	
	private static Pattern p = Pattern.compile("\\w+\\.");
	
	public static void main(String[] args) {
		if (args.length < 1) {
			print(usage);
			System.exit(0);
		}
		
		int lines = 0;
		
		try {
			Class<?> c = Class.forName(args[0]);
			Method[] methods = c.getMethods();
			Constructor<?>[] ctors = c.getConstructors();
			
			if (args.length == 1) {
				for (Method method : methods)  {
					printAccessibleObject(method);
				}
				
				for (Constructor<?> ctor : ctors) {
					printAccessibleObject(ctor);
				}
						
				lines = methods.length + ctors.length;
			} else {
				lines = printMatchingAccessibleObjects(methods, args[1], lines);
				lines = printMatchingAccessibleObjects(ctors, args[1], lines);
			}
		} catch (ClassNotFoundException e) {
			print("No such class: " + e);
		}
	}

	private static int printMatchingAccessibleObjects(AccessibleObject[] objects, String searchTerm, int lines) {
		for (AccessibleObject obj : objects) {
			if (obj.toString().indexOf(searchTerm) != -1) {
				printAccessibleObject(obj);
				lines++;
			}
		}
		return lines;
	}

	private static void printAccessibleObject(AccessibleObject obj) {
		print(p.matcher(obj.toString()).replaceAll(""));
	}
}