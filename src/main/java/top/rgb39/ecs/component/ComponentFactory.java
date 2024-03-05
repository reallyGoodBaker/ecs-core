package top.rgb39.ecs.component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import top.rgb39.ecs.annotation.Component;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/component/ComponentFactory.class */
public class ComponentFactory {
    private static Map<Class<?>, Object> singletons = new HashMap<>();

    public static Object getComponent(Class<?> componentClass) {
        Component annotation = (Component) componentClass.getAnnotation(Component.class);
        if (annotation.singleton()) {
            return getSingletonComponent(componentClass);
        }
        return getNormalComponent(componentClass);
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
