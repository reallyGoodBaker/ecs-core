package top.rgb39.ecs.executor;

import top.rgb39.ecs.util.Option;
import top.rgb39.ecs.util.Option.T;
import top.rgb39.ecs.annotation.System;;

public record SystemConfig(
    String runtimeLabel,
    boolean asynchronous
) {
    public static final T<SystemConfig> T = new T<SystemConfig>() {};

    public static Option<SystemConfig> from(System sys) {
        return sys == null
            ? Option.none(SystemConfig.T)
            : Option.some(new SystemConfig(sys.runtimeLabel(), sys.asynchronous()));
    }
}
