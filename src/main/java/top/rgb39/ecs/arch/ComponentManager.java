package top.rgb39.ecs.arch;

public interface ComponentManager {
    Object[] getComponents(long j);

    Object[] getSingletonComponents();

    <T> T getComponent(long j, Class<T> cls);

    <T> T getSingletonComponent(Class<T> cls);

    ComponentManager addComponent(long j, Object obj);

    ComponentManager addSingleComponent(Object obj);

    ComponentManager removeComponent(long j, Class<?> cls);

    ComponentManager removeSingletonComponent(Class<?> cls);
}
