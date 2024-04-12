package top.rgb39.ecs.loader;

import java.lang.reflect.Method;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.util.Option;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.executor.SystemConfig;
import top.rgb39.ecs.executor.SystemRecord;
import top.rgb39.ecs.annotation.System;
import top.rgb39.ecs.util.Systems;

public class SystemLoader {
    public static void load(App app) {
        for (Class<?> cls : Scanner.classes.values()) {
            for (Method method : cls.getDeclaredMethods()) {
                System sys = method.getAnnotation(System.class);
                var some = Option.it(SystemConfig.T);

                switch (SystemConfig.from(sys).state(some)) {
                    case NONE -> { continue; }
                    case SOME -> {
                        if (!SystemRecord.has(method)) {
                            app.addSystem(method, some.v());
                        }
                    }
                }

                Logger.ECS.i(Logger.light(32), "system added: " + Systems.getFullName(method) + " (" + sys.runtimeLabel() + ")", new Object[0]);
            }
        }
    }
}
