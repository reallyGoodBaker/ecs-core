package top.rgb39.ecs.arch;

import java.lang.reflect.Method;

import top.rgb39.ecs.executor.SystemConfig;

public interface SystemManager {
    SystemManager addSystem(Method system, SystemConfig sysConfig);
    SystemManager addSystem(Method system, String runtimeLabel);
    SystemManager addSystem(Method system);
    SystemManager removeSystem(Method system, String runtimeLabel);
    SystemManager removeSystem(Method system);
    SystemManager removeSystems(String runtimeLabel);
    boolean replaceSystem(Method system, Method old, String runtimeLabel);
    boolean replaceSystem(Method system, Method old);
    Method[] getSystems();
    Method[] getSystems(String runtimeLabel);
}
