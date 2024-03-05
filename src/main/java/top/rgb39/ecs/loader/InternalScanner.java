package top.rgb39.ecs.loader;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/loader/InternalScanner.class */
public class InternalScanner implements Scanner {
    public static void scan() throws Exception {
        ClassLoader cl = InternalScanner.class.getClassLoader();
        Enumeration<URL> urls = cl.getResources("top/rgb39/ecs");
        while (urls.hasMoreElements()) {
            URL jarFileURL = urls.nextElement();
            JarURLConnection urlConnection = (JarURLConnection) jarFileURL.openConnection();
            JarFile jarFile = urlConnection.getJarFile();
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    String className = entryName.replace("/", ".").replace(".class", "");
                    Class<?> clazz = cl.loadClass(className);
                    classes.put(clazz.getName(), clazz);
                }
            }
        }
    }
}
