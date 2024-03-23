package top.rgb39.ecs.loader;

import java.net.URL;

public class ClassFileScanner implements Scanner {
    public static void scan(URL classUrl) throws Exception {
        Class<?> cls = Thread.currentThread()
            .getContextClassLoader()
            .loadClass(classUrl.getPath().replace("/", "."));

        classes.put(cls.getName(), cls);
    }
}
