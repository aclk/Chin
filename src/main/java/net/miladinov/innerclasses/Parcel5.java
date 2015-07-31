package net.miladinov.innerclasses;
import static net.miladinov.util.Print.*;

public class Parcel5 {
	public Destination destination(String s) {
		class PDestination implements Destination {
			public String readLabel() {
				return label;
			}
			private String label;
			private PDestination(String whereTo) {
				label = whereTo;
			}
		}
		return new PDestination(s);
	}
	
	public static void main(String[] args) {
		Parcel5 p = new Parcel5();
		Destination d = p.destination("Tasmania");
		print(d);
	}
}
