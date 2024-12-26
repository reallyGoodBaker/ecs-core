package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.loader.ClassScanner;

public class ClassScannerPlugin implements Plugin {

    private final String[] names;

    public ClassScannerPlugin(String... names) {
        this.names = names;
    }

    public static ClassScannerPlugin create(String... names) {
        return new ClassScannerPlugin(names);
    }

    @Override
    public void build(App app) {
        for (String name : names) {
            try {
                ClassScanner.scan(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
