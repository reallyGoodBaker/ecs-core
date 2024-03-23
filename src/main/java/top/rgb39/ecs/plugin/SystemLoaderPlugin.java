package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.loader.SystemLoader;

public class SystemLoaderPlugin implements Plugin {

    @Override
    public void build(App app) {
        SystemLoader.load(app);
    }

}
