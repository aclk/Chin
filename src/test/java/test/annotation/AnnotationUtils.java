package test.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationUtils {
    public static void init(Object obj) throws InvocationTargetException,
            IllegalAccessException {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodAnnotation.class)) {
                System.out.println(method.getName());
                MethodAnnotation annotation = method
                        .getAnnotation(MethodAnnotation.class);
                Class<?> type = annotation.type();
                System.out.println(type);
                System.out.println(annotation.value());
                if (type.equals(java.lang.Integer.class)) {
                    method.invoke(obj, Integer.valueOf(annotation.value()));
                } else {
                    method.invoke(obj, annotation.value());
                }
            }
        }
    }
}
