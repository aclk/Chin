package net.miladinov.generics;
import java.util.*;

// You must provide distinct method names when the erased arguments do not produce a unique argument list:
public class UseList<W, T> {
    void f1(List<T> v) {}
    void f2(List<W> v) {}
}
// Fortunately, this kind of problem is detected by the compiler.
