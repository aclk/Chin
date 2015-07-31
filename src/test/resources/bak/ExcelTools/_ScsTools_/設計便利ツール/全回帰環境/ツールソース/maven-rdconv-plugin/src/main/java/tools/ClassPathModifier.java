package tools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * クラスパス動的書換ユティリティ
 * @author K.Miura
 * @version $Id$
 * @since JDK5.0
 */
public class ClassPathModifier {

    private final Class[] parameters = new Class[]{URL.class};

    private URLClassLoader sysloader;

    public ClassPathModifier() {
        sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
    }

    public ClassPathModifier(URLClassLoader classLoader) {
        sysloader = classLoader;
    }

    public void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    public void addFile(File f) throws IOException {
        addURL(f.toURL());
    }

    public void addURL(URL u) throws IOException {

        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{u});
        } catch (Throwable t) {
            throw new IOException("could not add " + u + " to classpath");
        }
    }

    public Object getClassInstance(String className)
        throws InstantiationException, IllegalAccessException,
        ClassNotFoundException {

        return sysloader.loadClass(className).newInstance();

    }
}
