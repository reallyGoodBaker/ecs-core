package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.App;

public class DefaultPlugins implements Plugin {

    private final String[] names;

    public DefaultPlugins(String... names) {
        this.names = names;
    }

    @Override
    public void build(App app) {
        app.addPlugins(
            new ClassScannerPlugin(names),
            new ParameterMatchers(),
            new RuntimePlugin(),
            new Events(),
            new SystemLoaderPlugin()
        );
    }

}
