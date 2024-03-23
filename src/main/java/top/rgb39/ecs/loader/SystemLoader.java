package top.rgb39.ecs.loader;

import java.lang.reflect.Method;
import java.util.Objects;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.annotation.System;
import top.rgb39.ecs.util.Systems;

public class SystemLoader {
    public static void load(App app) {
        for (Class<?> cls : Scanner.classes.values()) {
            for (Method method : cls.getDeclaredMethods()) {
                System sys = (System) method.getAnnotation(System.class);
                if (!Objects.isNull(sys)) {
                    app.addSystem(method, sys.runtimeLabel());
                    Logger.ECS.i(Logger.light(32), "system added: " + Systems.getFullName(method) + " (" + sys.runtimeLabel() + ")", new Object[0]);
                }
            }
        }
    }
}
