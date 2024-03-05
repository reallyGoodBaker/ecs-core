package top.rgb39.ecs;

import top.rgb39.ecs.arch.App;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/RuntimeSchedular.class */
public interface RuntimeSchedular {
    void schedule(RuntimeChain runtimeChain, App app);

    void cancel();
}
