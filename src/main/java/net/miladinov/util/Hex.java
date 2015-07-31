package net.miladinov.util;
import java.io.*;

public class Hex {
	public static String format(byte[] data) {
		StringBuilder result = new StringBuilder();
		int n = 0;
		for (byte b : data) {
			if (n % 16 == 0) 
				result.append(String.format("%05X: ", n));
			result.append(String.format("%02X ", b));
			n++;
			if (n % 16 == 0) result.append("\n");
		}
		result.append("\n");
		return result.toString();
	}
	
	public static void main(String[] args) {
		File file;
		if (args.length == 0) {
			file = new File("Hex.class");
		} else {
			file = new File(args[0]);
		}
//		System.out.println(format(BinaryFile.read(file)));
	}
}