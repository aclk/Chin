package net.miladinov.innerclasses.controller;

class WithInner {
	class Inner {}
}

public class InheritInner extends WithInner.Inner {
	InheritInner(WithInner wi) {
		wi.super();
	}
	public static void main(String[] args) {
		InheritInner ii = new InheritInner(new WithInner());
	}
}