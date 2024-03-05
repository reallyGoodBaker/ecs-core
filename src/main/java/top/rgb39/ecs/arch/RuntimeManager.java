package top.rgb39.ecs.arch;

import top.rgb39.ecs.RuntimeChain;
import top.rgb39.ecs.RuntimeSchedular;
import top.rgb39.ecs.SystemChain;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/arch/RuntimeManager.class */
public interface RuntimeManager {
    RuntimeManager setScheduler(RuntimeSchedular runtimeSchedular);

    void run();

    void stop();

    RuntimeManager setRuntimeChain(RuntimeChain runtimeChain);

    RuntimeChain getRuntimeChain();

    SystemChain getSystemChain(String str);

    RuntimeManager setSystemChain(String str, SystemChain systemChain);
}
