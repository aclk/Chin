package net.miladinov.exceptions;

public class WhatWIllItPrint {
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println("I iterated!");
				return;
			} finally {
				continue;
			}
		}
	}
}