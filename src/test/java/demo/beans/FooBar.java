package demo.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FooBar クラス.
 *
 * 内部で Bar クラスをネストしている.
 *
 * @author Kentaro Ohkouchi
 * @version $Revision$ $Date$
 */
public class FooBar {

    private String foo;
    private Bar bar;
    private String[] strings;
    private List bars;
    private Map map;

    public FooBar() {
        this.foo = "foo";
        bar = new Bar();
        strings = new String[]{"abc", "def", "ghi"};
        bars = new ArrayList();
        map = new HashMap();
        for (String string : strings) {
            bars.add(new Bar(string));
            map.put(string, string + " of value");
        }
    }

    public String getFoo() {
        return foo;
    }
    public void setFoo(String foo) {
        this.foo = foo;
    }
    public Bar getBar() {
        return bar;
    }
    public void setBar(Bar bar) {
        this.bar = bar;
    }
    public String[] getStrings() {
        return strings;
    }
    public void setStrings(String[] strings) {
        this.strings = strings;
    }
    public List getBars() {
        return bars;
    }
    public void setBars(List bars) {
        this.bars = bars;
    }
    public Map getMap() {
        return map;
    }
    public void setMap(Map map) {
        this.map = map;
    }
}