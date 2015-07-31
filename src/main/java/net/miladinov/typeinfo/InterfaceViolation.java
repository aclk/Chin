package net.miladinov.typeinfo;
import net.miladinov.typeinfo.interfacea.*;

class B implements A {
	public void f() {}
	public void g() {}
}

public class InterfaceViolation {
	public static void main(String[] args) {
		A a = new B();
		a.f();
		// a.g(); compile error
		System.out.println(a.getClass().getName());
		
		if (a instanceof B) {
			B b = (B)a;
			b.g();
		}
	}
}