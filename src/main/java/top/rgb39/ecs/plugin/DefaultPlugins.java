package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.loader.InternalScanner;

public class DefaultPlugins implements Plugin {

    private String[] names;

    public DefaultPlugins(String... names) {
        this.names = names;
    }

    @Override
    public void build(App app) {
        try {
            InternalScanner.scan("top/rgb39/ecs");
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.addPlugins(
            new ClassScannerPlugin(names),
            new ParameterMatchers(),
            new RuntimePlugin(),
            new SystemLoaderPlugin()
        );
    }

}
