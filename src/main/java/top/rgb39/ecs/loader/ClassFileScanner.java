package top.rgb39.ecs.loader;

import java.net.URL;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/loader/ClassFileScanner.class */
public class ClassFileScanner implements Scanner {
    public static void scan(URL classUrl) throws Exception {
        Class<?> cls = Thread.currentThread()
            .getContextClassLoader()
            .loadClass(classUrl.getPath().replace("/", "."));

        classes.put(cls.getName(), cls);
    }
}
