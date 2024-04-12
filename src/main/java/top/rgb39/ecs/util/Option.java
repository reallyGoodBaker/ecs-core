package top.rgb39.ecs.util;

import java.util.Objects;

public class Option<V> {

    public enum Opt {
        NONE, SOME
    }

    public interface T<K> {}
    public static class ValueHolder<E> {
        E value;

        public E v() {
            Objects.requireNonNull(value);
            return value;
        }
    }

    static private final Option<Object> None = new Option<>(null);
    public static final int NONE = 0;
    public static final int SOME = 1;

    public static int SOME(ValueHolder<?> holder) {
        return SOME;
    }

    public static <V> ValueHolder<V> it(T<V> type) {
        return new ValueHolder<>();
    }

    final private V value;

    @SuppressWarnings("unchecked")
    public static <V extends Object> Option<V> none(T<V> type) {
        return (Option<V>) None;
    }

    public static <V> Option<V> some(V value) {
        return new Option<V>(value);
    }

    private Option(V value) {
        this.value = value;
    }

    public V unwrap() {
        if (value == null) {
            throw new NullPointerException();
        }

        return value;
    }

    public Opt state() {
        return value == null ? Opt.NONE : Opt.SOME;
    }

    public Opt state(ValueHolder<V> holder) {
        holder.value = value;
        return value == null ? Opt.NONE : Opt.SOME;
    }

}
