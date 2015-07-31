package net.miladinov.generics;
import java.io.*;
import java.util.*;

public class NeedCasting {
    @SuppressWarnings("unchecked")
    public void f(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));
        List<Widget> shapes = (List<Widget>)in.readObject();
    }
}

/*
As you no doubt already know, readObject() cannot know what it is reading,
so it returns an object that must be cast. But when you comment out the @SuppressWarnings
annotation and compile the program, you get a warning:

Note: NeedCasting.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.

And if you follow the instructions and recompile with -Xlint:unchecked:

NeedCasting.java:12: warning: [unchecked] unchecked cast
found   : java.lang.Object
required: java.util.List<Widget>
    List<Shape> shapes = (List<Widget>)in.readObject();

You're forced to cast, and yet you're told you shouldn't. To solve the problem, you must use a
new form of cast introduced in Java SE5, the cast via a generic class
 */
