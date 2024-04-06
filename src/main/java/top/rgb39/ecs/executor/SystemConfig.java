package top.rgb39.ecs.executor;

import top.rgb39.ecs.annotation.System;

public record SystemConfig(
    String runtimeLabel,
    boolean asynchronous
) {
    public static SystemConfig from(System sys) {
        return sys == null ? null : new SystemConfig(sys.runtimeLabel(), sys.asynchronous());
    }
}
