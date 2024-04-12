package top.rgb39.ecs.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SystemInstanceRecord {
    private static Map<Class<?>, Object> instRecord = new HashMap<>();

    public static void setInst(Class<?> clazz, Object instance) {
        instRecord.put(clazz, instance);
    }

    public static Object getInst(Class<?> clazz) {
        return instRecord.get(clazz);
    }

    public static Object getInstance(Class<?> clazz) {
        Object instance = instRecord.get(clazz);
        if (Objects.nonNull(instance)) {
            return instance;
        }
        try {
            Object obj = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            instRecord.put(clazz, obj);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
