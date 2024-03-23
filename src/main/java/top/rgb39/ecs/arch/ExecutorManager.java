package top.rgb39.ecs.arch;

public interface ExecutorManager {
    void setRuntimeManager(RuntimeManager manager);
    RuntimeManager getRuntimeManager();
    void run();
    void stop();
}
