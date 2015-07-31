package net.miladinov.exceptions;
import java.io.*;

public class MainException {
	// pass all exceptions to the console:
	public static void main(String[] args) throws Exception {
		// Open the file:
		FileInputStream file = new FileInputStream("/home/danielm/Programming/JavaProjects/TIJ4/src/net/miladinov/exceptions/MainException.java");
		// Use the file...
		// Close the file:
		file.close();
	}
}