package top.rgb39.ecs.arch;

import javax.annotation.Nonnull;

import top.rgb39.ecs.executor.RuntimeChain;
import top.rgb39.ecs.executor.RuntimeScheduler;
import top.rgb39.ecs.executor.SystemChain;

public interface RuntimeManager {
    RuntimeManager setScheduler(RuntimeScheduler runtimeScheduler);
    void run(App app);
    void stop();
    RuntimeManager setRuntimeChain(RuntimeChain runtimeChain);
    RuntimeChain getRuntimeChain();
    SystemChain getSystemChain(String str);
    RuntimeManager setSystemChain(String str, SystemChain systemChain);
    String currentRuntimeLabel();
    boolean isRuntimeLabel(@Nonnull String str);
}
