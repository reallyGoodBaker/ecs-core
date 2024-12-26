package top.rgb39.ecs.component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.util.Types;

public class ComponentFactory {
    private static final Map<Class<?>, Object> singletons = new HashMap<>();

    public static <T> T getComponent(Class<T> componentClass) {
        Component annotation = componentClass.getAnnotation(Component.class);
        if (Objects.isNull(annotation)) {
            return null;
        }
        if (annotation.singleton()) {
            return Types.cast(getSingletonComponent(componentClass));
        }
        return Types.cast(getNormalComponent(componentClass));
    }

    private static Object getNormalComponent(Class<?> componentClass) {
        try {
            Constructor<?> ctor = componentClass.getDeclaredConstructor(new Class[0]);
            ctor.setAccessible(true);
            return ctor.newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    private static Object getSingletonComponent(Class<?> componentClass) {
        Object obj = singletons.get(componentClass);
        if (Objects.nonNull(obj)) {
            return obj;
        }
        Object obj2 = getNormalComponent(componentClass);
        singletons.put(componentClass, obj2);
        return obj2;
    }
}
