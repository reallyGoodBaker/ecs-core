package top.rgb39.ecs.util;

import java.lang.reflect.Method;
import java.util.Objects;
import top.rgb39.ecs.annotation.System;

public class Systems {
    public static String getFullName(Method system) {
        System sys = (System) system.getAnnotation(System.class);
        if (Objects.isNull(sys)) {
            return null;
        }
        Class<?> cls = system.getDeclaringClass();
        return cls.getCanonicalName() + "." + system.getName();
    }
}
