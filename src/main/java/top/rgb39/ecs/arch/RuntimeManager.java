package top.rgb39.ecs.arch;

import top.rgb39.ecs.executor.RuntimeChain;
import top.rgb39.ecs.executor.RuntimeSchedular;
import top.rgb39.ecs.executor.SystemChain;

public interface RuntimeManager {
    RuntimeManager setScheduler(RuntimeSchedular runtimeSchedular);
    void run(App app);
    void stop();
    RuntimeManager setRuntimeChain(RuntimeChain runtimeChain);
    RuntimeChain getRuntimeChain();
    SystemChain getSystemChain(String str);
    RuntimeManager setSystemChain(String str, SystemChain systemChain);
}
