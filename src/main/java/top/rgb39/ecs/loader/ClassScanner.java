package top.rgb39.ecs.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.stream.Stream;

import top.rgb39.ecs.util.Logger;

import java.nio.file.*;

public class ClassScanner implements Scanner {

    static ClassLoader cl = Thread.currentThread().getContextClassLoader();

    public static void scan(String name) {
        Enumeration<URL> enums;
        try {
            enums = cl.getResources(name);
        } catch (IOException e) {
            return;
        }

        while (enums.hasMoreElements()) {
            URL url = enums.nextElement();

            if ("jar".equals(url.getProtocol())) {
                try {
                    JarScanner.scan(url);
                } catch(Exception ignored) {}
                continue;
            }

            if ("file".equals(url.getProtocol())) {
                String path = url.getPath().substring(1);
                Stream<Path> files;
                try {
                    files = Files.walk(Path.of(path));
                } catch (IOException e) {
                    continue;
                }

                files.filter(Files::isRegularFile)
                    .forEach(file -> {
                        String className = file.toString()
                            .replace("\\", "/");

                        int index = className.indexOf(name);
                        if (index == -1) {
                            return;
                        }

                        className = className.substring(index)
                            .replace(".class", "")
                            .replace("/", ".");

                        java.lang.System.out.printf("Loading class %s\n", className);

                        try {
                            Class<?> clz = cl.loadClass(className);
                            classes.put(clz.getName(), clz);
                        } catch (Exception e) {
                            Logger.ECS.e("%s", e);
                        }
                    });

                files.close();
            }
        
        }
    }

}
