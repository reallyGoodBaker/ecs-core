package top.rgb39.ecs.util;

import java.util.List;
import java.util.function.Predicate;

public class Lists {
    public static <T> T find(List<T> list, Predicate<T> predicate) {
        for (int i = 0; i < list.size(); i++) {
            T element = list.get(i);
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }
}
