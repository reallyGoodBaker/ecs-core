package top.rgb39.ecs.loader;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
        Files.walk(Path.of(fileUrl.toURI()), 10).forEach(file -> {
            loadFile(file);
        });
    }

    private static void loadFile(Path filePath) {
        try {
            File file = filePath.toFile();
            if (
                file.exists() &&
                file.isFile() &&
                file.canRead() &&
                filePath.toString().endsWith(".class")
            ) {
                System.out.println(filePath);
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
