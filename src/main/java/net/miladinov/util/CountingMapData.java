package net.miladinov.util;
import java.util.*;

public class CountingMapData {
    private int size;
    private static String[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

    public CountingMapData(int size) {
        if (size < 0) {
            this.size = 0;
        } else {
            this.size = size;
        }
    }

    public Object put(Object key, Object value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set entrySet() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
