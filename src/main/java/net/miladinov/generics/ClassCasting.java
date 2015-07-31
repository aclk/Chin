package net.miladinov.generics;
import java.io.*;
import java.util.*;

public class ClassCasting {
    @SuppressWarnings("unchecked")
    public void f(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));
        // Won't compile:
        // List<Widget> lw1 = List<Widget>.class.cast(in.readObject());
        List<Widget> lw2 = List.class.cast(in.readObject());
    }
}

/*
However, you can't cast to the actual type(List<Widget>). That is, you can't say
List<Widget>.class.cast(in.readObject())
and even if you add another cast like this:
(List<Widget>)List.class.cast(in.readObject())
you'll still get a warning.
 */
