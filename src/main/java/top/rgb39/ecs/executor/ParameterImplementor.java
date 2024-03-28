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
    private Object[] args;
    private static final Object EMPTY = new Object();

    public ParameterImplementor(Parameter[] params) {
        this.params = params;
        args = new Object[params.length];
        Arrays.fill(args, EMPTY);
    }

    public void set(int index, Object value) {
        args[index] = value;
    }

    public Object get(int index) {
        return args[index];
    }

    public Object invoke(Object target, Method method) throws Exception {
        if (Arrays.asList(args).contains(EMPTY)) {
            return null;
        }
        return method.invoke(target, args.clone());
    }

    public ParameterImplementor matchArgumentsOnlyReflect(App app) {
        for (int i = 0; i < this.params.length; i++) {
            Parameter param = this.params[i];
            matchReflects(args, param, i, app, null);
        }
        return this;
    }

    public ParameterImplementor matchArguments(App app, long entityId) {
        int i = 0;
        while (i < this.params.length) {
            Parameter param = this.params[i];
            for (ParameterMatcher matcher : matchers) {
                if (matcher.match(Arrays.asList(args), param, i, app, entityId)) {
                    i++;
                }
            }
        }
        return this;
    }

    private boolean matchReflects(Object[] args, Parameter param, int index, App app, @Nullable Long entityId) {
        Reflect reflect = (Reflect) param.getAnnotation(Reflect.class);
        
        if (Objects.isNull(reflect)) {
            return false;
        }

        Class<?> targetClass = reflect.value();
        Object target = app.getSingletonComponent(targetClass);

        if (Objects.nonNull(target)) {
            args[index] = target;
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
