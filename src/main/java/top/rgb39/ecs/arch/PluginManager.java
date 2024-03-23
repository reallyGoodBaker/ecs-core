package top.rgb39.ecs.arch;

import top.rgb39.ecs.plugin.Plugin;

public interface PluginManager {
    PluginManager addPlugin(Plugin plugin);
    PluginManager addPlugins(Plugin... plugins);
    boolean pluginsReady();
}
