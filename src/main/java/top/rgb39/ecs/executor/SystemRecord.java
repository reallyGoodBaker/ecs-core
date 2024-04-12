package top.rgb39.ecs.executor;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import top.rgb39.ecs.util.Maps;
import top.rgb39.ecs.util.Maps.OptionalMap;
import top.rgb39.ecs.util.Option;

public class SystemRecord {
    private final static OptionalMap<Method, SystemConfig> configRecord = Maps.optionalify(new ConcurrentHashMap<>());
    private final static OptionalMap<String, Method> systemRecord = Maps.optionalify(new ConcurrentHashMap<>());

    private static String getMethodName(Method method) {
        return "%s$%s".formatted(method.getDeclaringClass().getCanonicalName(), method.getName());
    }

    public static void record(Method method, SystemConfig config) {
        systemRecord.put(
            getMethodName(method),
            method
        );
        configRecord.put(method, config);
    }

    public static Option<SystemConfig> getConfig(Method method) {
        return configRecord.get(method);
    }

    public static Option<Method> getSystem(String methodName) {
        return systemRecord.get(methodName);
    }

    public static boolean has(Method method) {
        return systemRecord.containsKey(getMethodName(method));
    }
}
