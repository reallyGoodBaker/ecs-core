package top.rgb39.ecs.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Maps {

    public static <K, V> OptionalMap<K, V> optionalify(Map<K, V> map) {
        return new OptionalMap<>(map);
    }

    public static class OptionalMap<K, V> {

        final private Map<K, V> map;

        OptionalMap(Map<K, V> map) {
            this.map = map;
        }

        public int size() {
            return map.size();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        public Option<V> get(Object key) {
            return Option.some(map.get(key));
        }

        public V put(K key, V value) {
            return map.put(key, value);
        }

        public V remove(Object key) {
            return map.remove(key);
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            map.putAll(m);
        }

        public void clear() {
            map.clear();
        }

        public Set<K> keySet() {
            return map.keySet();
        }

        public Collection<V> values() {
            return map.values();
        }

        public Set<Entry<K, V>> entrySet() {
            return map.entrySet();
        }

    }
}
