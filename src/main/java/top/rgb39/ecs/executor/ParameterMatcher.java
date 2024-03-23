package top.rgb39.ecs.executor;

import java.lang.reflect.Parameter;
import java.util.List;

import javax.annotation.Nullable;

import top.rgb39.ecs.arch.App;

public interface ParameterMatcher {
    boolean match(
        List<Object> args,
        Parameter param,
        int argIndex,
        App app,
        @Nullable Long entityId
    );
}
