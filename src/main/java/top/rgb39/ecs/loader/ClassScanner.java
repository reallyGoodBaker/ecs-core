package top.rgb39.ecs.loader;

import java.net.URL;
import java.util.Enumeration;

import top.rgb39.ecs.util.Logger;

import java.nio.file.*;

public class ClassScanner implements Scanner {

    public static void scan(Class<?> cls, String name) throws Exception {
        Enumeration<URL> enums = cls.getClassLoader().getResources(name);
        String root = cls.getResource("/").getPath().substring(1);
        ClassLoader cl = cls.getClassLoader();

        while (enums.hasMoreElements()) {
            URL url = enums.nextElement();

            if ("jar".equals(url.getProtocol())) {
                JarScanner.scan(url);
                continue;
            }

            if ("file".equals(url.getProtocol())) {
                String path = url.getPath().substring(1);
                Files.walk(Path.of(path))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String className = file.toString()
                            .replace("\\", "/")
                            .replace(root, "")
                            .replace(".class", "")
                            .replace("/", ".");

                        Logger.ECS.err("Loading class %s -> %s", root, className);

                        try {
                            Class<?> clz = cl.loadClass(className);
                            classes.put(clz.getName(), clz);
                        } catch (ClassNotFoundException e) {
                            Logger.ECS.err("%s", e);
                        }
                    });
            }
        }
    }

}