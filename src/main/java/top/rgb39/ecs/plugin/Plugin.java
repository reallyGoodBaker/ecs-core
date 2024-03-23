package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.App;

public interface Plugin {
    void build(App app);

    default boolean ready() {
        return true;
    }
}
