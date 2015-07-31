package net.miladinov.exceptions;

class SimpleException extends Exception { private static final long serialVersionUID = 1L; };

public class InheritingExceptions {
	public void f() throws SimpleException {
		System.out.println("Throw SimpleException from f()");
		throw new SimpleException();
	}
	
	public static void main(String[] args) {
		InheritingExceptions sed = new InheritingExceptions();
		
		try {
			sed.f();
		} catch (SimpleException se) {
			System.out.println("Caught it!");
		}
	}
}