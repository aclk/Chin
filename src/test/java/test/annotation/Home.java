package test.annotation;

import java.lang.reflect.InvocationTargetException;

@ClassAnnotation(name = "andy")
public class Home {

    public Home() throws InvocationTargetException, IllegalAccessException {
        super();
        AnnotationUtils.init(this);
    }

    @FiledAnnotation(value = "name")
    private String name;

    @FiledAnnotation(value = "7")
    private int count;

    public String getName() {
        return name;
    }

    @MethodAnnotation(value = "andy")
    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    @MethodAnnotation(type = java.lang.Integer.class, value = "7")
    public void setCount(int count) {
        this.count = count;
    }
}