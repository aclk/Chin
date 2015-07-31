package net.miladinov.generics;

public class SimpleHolder {
    public Object obj;
    public void set(Object obj) { this.obj = obj; }
    public Object get() { return obj; }

    public static void main(String[] args) {
        SimpleHolder holder = new SimpleHolder();
        holder.set("Item");
        String s = (String)holder.get();
        System.out.println(s);
    }
}
