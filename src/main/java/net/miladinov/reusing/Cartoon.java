package net.miladinov.reusing;

class Art {
	Art() { System.out.println("Art constructor"); }
	public void noOp() {}
}
	
class Drawing extends Art {
	Drawing () { System.out.println("Drawing constructor"); }
}
	
public class Cartoon extends Drawing {
	public Cartoon() { 
		super();
		System.out.println("Cartoon constructor"); 
	}
	
	public static void main(String[] args) {
		Cartoon x = new Cartoon();
		x.noOp();
	}
}