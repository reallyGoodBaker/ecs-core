package top.rgb39.ecs.loader;

import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InternalScanner implements Scanner {
    private static final ClassLoader cl = InternalScanner.class.getClassLoader();

    public static void scan(String name) throws Exception {
        Enumeration<URL> urls = cl.getResources(name);
        while (urls.hasMoreElements()) {
            URL fileURL = urls.nextElement();
            URI fileUri = fileURL.toURI();

            if ("jar".equals(fileUri.getScheme())) {
                JarURLConnection urlConnection = (JarURLConnection) fileURL.openConnection();
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

    public static void scan(String... names) throws Exception {
        for (String n : names) {
            scan(n);
        }
    }
}
