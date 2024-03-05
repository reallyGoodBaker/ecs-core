package top.rgb39.ecs.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/loader/Scanner.class */
public interface Scanner {
    public static final Map<String, Class<?>> classes = new HashMap<>();

    static List<Class<?>> filter(Predicate<? super Class<?>> predicate) {
        return classes.values().stream().filter(predicate).toList();
    }
}