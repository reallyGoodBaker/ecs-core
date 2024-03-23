package top.rgb39.ecs.executor;

import top.rgb39.ecs.arch.App;

public interface RuntimeSchedular {
    void schedule(RuntimeChain runtimeChain, App app);

    void cancel();
}
