// Guaranteeing proper cleanup of a resource
package net.miladinov.exceptions;

public class Cleanup {
	public static void main(String[] args) {
		try {
			InputFile in = new InputFile("Cleanup.java");
			try {
				String s;
				while ((s = in.getLine()) != null) {
					; // Perform line-by-line processing here...
				}
			} catch (Exception e) {
				System.out.println("Caught exception in main");
				e.printStackTrace(System.out);
			} finally {
				in.dispose();
			}
		} catch (Exception e) {
			System.out.println("InputFile construction failed");
		}
	}
}