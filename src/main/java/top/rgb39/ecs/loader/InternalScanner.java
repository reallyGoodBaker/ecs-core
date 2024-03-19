package top.rgb39.ecs.loader;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/loader/InternalScanner.class */
public class InternalScanner implements Scanner {
    public static void scan(String name) throws Exception {
        ClassLoader cl = InternalScanner.class.getClassLoader();
        Enumeration<URL> urls = cl.getResources(name);
        while (urls.hasMoreElements()) {
            URL fileURL = urls.nextElement();
            String filePath = fileURL.getFile();

            if (filePath.startsWith("file:")) {
                loadFiles(fileURL);
                continue;
            }

            if (filePath.startsWith("jar:")) {
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
                continue;
            }
        }
    }

    private static void loadFiles(URL fileUrl) throws Exception {
        File f = new File(fileUrl.toURI());

        if (!f.exists()) {
            return;
        }

        if (f.isFile()) {
            loadFile(f);
            return;
        }

        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                loadFile(file);
            }
        }
    }

    private static void loadFile(File file) {
        try {
            if (
                file.exists() &&
                file.isFile() &&
                file.canRead() &&
                file.toString().endsWith(".class")
            ) {
                System.out.println(file);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void scan(String... names) throws Exception {
        for (String n : names) {
            scan(n);
        }
    }
}
