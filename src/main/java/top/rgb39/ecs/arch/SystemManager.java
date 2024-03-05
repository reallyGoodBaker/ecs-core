package top.rgb39.ecs.arch;

import java.lang.reflect.Method;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/arch/SystemManager.class */
public interface SystemManager {
    RuntimeManager addSystem(Method method, String str);

    RuntimeManager addSystem(Method method);

    RuntimeManager removeSystem(Method method, String str);

    RuntimeManager removeSystem(Method method);

    RuntimeManager removeSystems(String str);

    boolean replaceSystem(Method method, Method method2, String str);

    boolean replaceSystem(Method method, Method method2);

    Method[] getSystems();

    Method[] getSystems(String str);
}
