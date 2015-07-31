package net.miladinov.generics;
import java.util.*;

interface TimeStamped { long getStamp(); }

class TimeStampedImplementer implements TimeStamped {
    private final long timeStamp;

    public TimeStampedImplementer() {
        timeStamp = new Date().getTime();
    }

    public long getStamp() {
        return timeStamp;
    }
}

interface SerialNumbered { long getSerialNumber(); }

class SerialNumberedImplementer implements SerialNumbered {
    private static long counter = 1;
    private final long serialNumber = counter++;
    public long getSerialNumber() {
        return serialNumber;
    }
}

interface Basic  {
    public void set(String val);
    public String get();
}

class BasicImplementer implements Basic {
    private String value;

    public void set(String val) {
        value = val;
    }

    public String get() {
        return value;
    }
}

class Mixin extends BasicImplementer implements TimeStamped, SerialNumbered {
    private SerialNumbered serialNumber = new SerialNumberedImplementer();
    private TimeStamped timeStamp = new TimeStampedImplementer();

    public long getSerialNumber() {
        return serialNumber.getSerialNumber();
    }

    public long getStamp() {
        return timeStamp.getStamp();
    }
}

public class Mixins {
    public static void main(String[] args) {
        Mixin mixin1 = new Mixin(), mixin2 = new Mixin();
        mixin1.set("test string 1");
        mixin2.set("test string 2");
        System.out.println(mixin1.get() + " " + mixin1.getStamp() + " " + mixin1.getSerialNumber());
        System.out.println(mixin2.get() + " " + mixin2.getStamp() + " " + mixin1.getSerialNumber());
    }
}
