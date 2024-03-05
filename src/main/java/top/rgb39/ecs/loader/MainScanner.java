package top.rgb39.ecs.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Objects;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/loader/MainScanner.class */
public class MainScanner implements Scanner {
    public static void scan(String pathname) throws Exception {
        Path rootUrl = new File(pathname).toPath();
        if (!Files.exists(rootUrl, new LinkOption[0])) {
            Files.createDirectory(rootUrl, new FileAttribute[0]);
        }
        List<URL> files = Files.walk(Paths.get(rootUrl.toUri()), new FileVisitOption[0]).filter(Files::isExecutable).map(f -> {
            try {
                return f.toUri().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).toList();
        for (URL file : files) {
            if (file.getPath().endsWith(".class")) {
                ClassFileScanner.scan(file);
            }
            if (file.getPath().endsWith(".jar")) {
                JarScanner.scan(URI.create("jar: " + file.toString() + "!/").toURL());
            }
        }
    }
}
