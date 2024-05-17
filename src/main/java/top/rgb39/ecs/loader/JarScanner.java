package top.rgb39.ecs.loader;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import top.rgb39.ecs.util.Logger;

public class JarScanner implements Scanner {
    public static void scan(URL jarFileURL) throws Exception {
        var ucl = new URLClassLoader(new URL[]{jarFileURL});
        var urlConnection = (JarURLConnection) jarFileURL.openConnection();
        var jarFile = urlConnection.getJarFile();
        var jarEntryEnumeration = jarFile.entries();
        while (jarEntryEnumeration.hasMoreElements()) {
            var jarEntry = jarEntryEnumeration.nextElement();
            var jarEntryName = jarEntry.getName();
            if (!jarEntry.isDirectory() && jarEntryName.endsWith(".class") && !jarEntryName.contains("$") && !jarEntryName.contains("META-INF") && !jarEntryName.contains("module-info")) {
                try {
                    var className = jarEntryName.replace("/", ".").replace(".class", "");
                    Logger.ECS.i("Loading class %s".formatted(className));
                    var cls = ucl.loadClass(className);
                    classes.put(cls.getName(), cls);
                } catch (Exception e) {
                    Logger.ECS.e("JarScanner: %s".formatted(e));
                }
            }
        }
        ucl.close();
    }
}
