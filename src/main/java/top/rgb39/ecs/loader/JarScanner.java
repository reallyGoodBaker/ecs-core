package top.rgb39.ecs.loader;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/loader/JarScanner.class */
public class JarScanner implements Scanner {
    public static void scan(URL jarFileURL) throws Exception {
        URLClassLoader ucl = new URLClassLoader(new URL[]{jarFileURL});
        JarURLConnection urlConnection = (JarURLConnection) jarFileURL.openConnection();
        JarFile jarFile = urlConnection.getJarFile();
        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            String jarEntryName = jarEntry.getName();
            if (!jarEntry.isDirectory() && jarEntryName.endsWith(".class") && !jarEntryName.contains("$") && !jarEntryName.contains("META-INF") && !jarEntryName.contains("module-info")) {
                try {
                    Class<?> cls = ucl.loadClass(jarEntryName.replace("/", ".").replace(".class", ""));
                    classes.put(cls.getName(), cls);
                } catch (Exception e) {
                }
            }
        }
        ucl.close();
    }
}
