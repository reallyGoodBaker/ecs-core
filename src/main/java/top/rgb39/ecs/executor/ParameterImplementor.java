package top.rgb39.ecs.executor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.annotation.Reflect;

public class ParameterImplementor {
    private final Parameter[] params;
    private final List<Object> args;
    private static final Object EMPTY = new Object();

    public ParameterImplementor(Parameter[] params) {
        this.params = params;
        Object[] _args = new Object[params.length];
        Arrays.fill(_args, EMPTY);
        this.args = Arrays.asList(_args);
    }

    public void set(int index, Object value) {
        this.args.set(index, value);
    }

    public Object get(int index) {
        return this.args.get(index);
    }

    public Object invoke(Object target, Method method) throws Exception {
        if (this.args.contains(EMPTY)) {
            return null;
        }
        return method.invoke(target, this.args.toArray());
    }

    public ParameterImplementor matchArgumentsOnlyReflect(App app) {
        for (int i = 0; i < this.params.length; i++) {
            Parameter param = this.params[i];
            matchReflects(args, param, i, app, null);
        }
        return this;
    }

    public ParameterImplementor matchArguments(App app, long entityId) {
        for (int i = 0; i < params.length; i++) {
            Parameter param = this.params[i];
            for (ParameterMatcher matcher : matchers) {
                if (matcher.match(args, param, i, app, entityId)) {
                    break;
                }
            }
        }

        return this;
    }

    private boolean matchReflects(List<Object> args, Parameter param, int index, App app, @Nullable Long entityId) {
        Reflect reflect = (Reflect) param.getAnnotation(Reflect.class);
        
        if (Objects.isNull(reflect)) {
            return false;
        }

        Class<?> targetClass = reflect.value();
        Object target = app.getSingletonComponent(targetClass);

        if (Objects.nonNull(target)) {
            args.set(index, target);
            return true;
        }

        return false;
    }

    private static final List<ParameterMatcher> matchers = new ArrayList<>();

    public static void registerMatcher(ParameterMatcher matcher) {
        matchers.add(matcher);
    }

    public static void registerMatchers(ParameterMatcher... matcher) {
        matchers.addAll(Arrays.asList(matcher));
    }

}
