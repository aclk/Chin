package net.miladinov.typeinfo.packageaccess;
import net.miladinov.typeinfo.interfacea.*;
import static net.miladinov.util.Print.*;

class C implements A { 
	public void f() { print("public C.f()"); }
	public void g() { print("public C.g()"); }
	void u() { print("package C.u()"); }
	protected void v() { print("protected C.v()"); }
	private void w() { print("private C.w()"); }
}

public class HiddenC {
	public static A makeA() { return new C(); }
}
