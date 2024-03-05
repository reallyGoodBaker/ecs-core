package top.rgb39.ecs.arch;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/arch/ComponentManager.class */
public interface ComponentManager {
    Object[] getComponents(long j);

    Object[] getSingletonComponents();

    Object getComponent(long j, Class<?> cls);

    Object getSingletonComponent(Class<?> cls);

    ComponentManager addComponent(long j, Object obj);

    ComponentManager addSingleComponent(Object obj);

    ComponentManager removeComponent(long j, Class<?> cls);

    ComponentManager removeSingletonComponent(Class<?> cls);
}
