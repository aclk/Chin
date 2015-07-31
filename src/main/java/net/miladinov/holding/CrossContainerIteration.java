package net.miladinov.holding;
import net.miladinov.typeinfo.pets.*;
import java.util.*;

public class CrossContainerIteration {
	public static void display(Iterator<Pet> it) {
		while (it.hasNext()) {
			Pet p = it.next();
			System.out.print(p.id() + ":" + p + " ");
		}
	}
}
