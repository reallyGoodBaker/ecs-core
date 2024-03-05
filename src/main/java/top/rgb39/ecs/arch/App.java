package top.rgb39.ecs.arch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.Nullable;
import top.rgb39.ecs.RuntimeChain;
import top.rgb39.ecs.RuntimeLabel;
import top.rgb39.ecs.RuntimeSchedular;
import top.rgb39.ecs.SystemChain;
import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.component.ComponentFactory;
import top.rgb39.ecs.loader.InternalScanner;
import top.rgb39.ecs.util.Lists;
import top.rgb39.ecs.util.Logger;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/arch/App.class */
public class App implements EntityManager, ComponentManager, RuntimeManager, SystemManager {
    @Nullable
    public final Table table = new Table();
    public final List<Object> singletonComponents = new ArrayList<>();
    private RuntimeSchedular schedular;
    private RuntimeChain runtimeChain;

    private App() {
        try {
            InternalScanner.scan("top/rgb39/ecs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void defaultRuntimeChainInit() {
        SystemChain beforeUpdate = new SystemChain();
        SystemChain event = new SystemChain();
        SystemChain update = new SystemChain();
        SystemChain afterUpdate = new SystemChain();
        SystemChain startup = new SystemChain();
        this.runtimeChain = new RuntimeChain() { // from class: top.yuumo.meisterhau.lib.arch.App.1
            @Override // top.yuumo.meisterhau.lib.ecs.RuntimeChain
            public String[] getScheduleSequence() {
                return new String[]{RuntimeLabel.Event, RuntimeLabel.BeforeUpdate, RuntimeLabel.Update, RuntimeLabel.AfterUpdate};
            }
        };
        this.runtimeChain.setSystemChain(RuntimeLabel.Event, event);
        this.runtimeChain.setSystemChain(RuntimeLabel.BeforeUpdate, beforeUpdate);
        this.runtimeChain.setSystemChain(RuntimeLabel.Update, update);
        this.runtimeChain.setSystemChain(RuntimeLabel.AfterUpdate, afterUpdate);
        this.runtimeChain.setSystemChain(RuntimeLabel.Startup, startup);
    }

    public void defaultRuntimeSchedulerInit() {
        setScheduler(new DefaultRuntimeScheduler());
    }

    @Override // top.yuumo.meisterhau.lib.arch.EntityManager
    public EntityManager addEntity(long entityId, Object... components) {
        for (Object obj : components) {
            ((Table) Objects.requireNonNull(this.table)).setCell(obj.getClass(), entityId, obj);
        }
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.EntityManager
    public EntityManager removeEntity(long entityId) {
        ((Table) Objects.requireNonNull(this.table)).deleteRow(entityId);
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public Object[] getComponents(long entityId) {
        return ((Table) Objects.requireNonNull(this.table)).getRowArray(entityId);
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public Object[] getSingletonComponents() {
        return singletonComponents.toArray();
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public Object getComponent(long entityId, Class<?> componentClass) {
        Object component = ((Table) Objects.requireNonNull(this.table)).getCell(componentClass, entityId);
        if (Objects.nonNull(component)) {
            return component;
        }
        Object component2 = ComponentFactory.getComponent(componentClass);
        ((Table) Objects.requireNonNull(this.table)).setCell(componentClass, entityId, component2);
        return component2;
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public Object getSingletonComponent(Class<?> componentClass) {
        Object component = Lists.find(singletonComponents, c -> {
            Logger.info(c.getClass().getName());
            return c.getClass().equals(componentClass);
        });
        if (Objects.nonNull(component)) {
            return component;
        }
        Object component2 = ComponentFactory.getComponent(componentClass);
        if (Objects.nonNull(component2)) {
            singletonComponents.add(component2);
        }
        return component2;
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public ComponentManager addComponent(long entityId, Object component) {
        Component c = component.getClass().getAnnotation(Component.class);
        if (Objects.nonNull(c) && c.singleton()) {
            addSingleComponent(component);
        }
        ((Table) Objects.requireNonNull(this.table)).setCell(component.getClass(), entityId, component);
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public ComponentManager removeComponent(long entityId, Class<?> componentClass) {
        ((Table) Objects.requireNonNull(this.table)).deleteCell(componentClass, entityId);
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.ComponentManager
    public ComponentManager removeSingletonComponent(Class<?> componentClass) {
        Object component = Lists.find(this.singletonComponents, c -> {
            return c.getClass().equals(componentClass);
        });
        if (Objects.nonNull(component)) {
            singletonComponents.remove(component);
        }
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public RuntimeManager setScheduler(RuntimeSchedular schedular) {
        this.schedular = schedular;
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public void run() {
        if (Objects.nonNull(this.schedular)) {
            this.schedular.schedule(this.runtimeChain, this);
        }
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public void stop() {
        if (Objects.nonNull(this.schedular)) {
            this.schedular.cancel();
        }
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public RuntimeManager addSystem(Method system, String label) {
        system.setAccessible(true);
        SystemChain chain = this.runtimeChain.getSystemChain(label);
        if (Objects.nonNull(chain)) {
            chain.addSystem(system);
        }
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public RuntimeManager addSystem(Method system) {
        return addSystem(system, RuntimeLabel.Update);
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public RuntimeManager removeSystem(Method system, String label) {
        SystemChain chain = this.runtimeChain.getSystemChain(label);
        if (Objects.nonNull(chain)) {
            chain.removeSystem(system);
        }
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public RuntimeManager removeSystem(Method system) {
        return removeSystem(system, RuntimeLabel.Update);
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public boolean replaceSystem(Method system, Method old, String label) {
        SystemChain chain = this.runtimeChain.getSystemChain(label);
        if (Objects.nonNull(chain)) {
            chain.replaceSystem(system, old);
            return true;
        }
        return false;
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public boolean replaceSystem(Method system, Method old) {
        return replaceSystem(system, old, RuntimeLabel.Update);
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public Method[] getSystems() {
        return getSystems(RuntimeLabel.Update);
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public Method[] getSystems(String label) {
        SystemChain chain = this.runtimeChain.getSystemChain(label);
        if (Objects.isNull(chain)) {
            return new Method[0];
        }
        return chain.getSystems();
    }

    public static App empty() {
        return new App();
    }

    public static App create() {
        App app = new App();
        app.defaultRuntimeChainInit();
        app.defaultRuntimeSchedulerInit();
        return app;
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public RuntimeManager setRuntimeChain(RuntimeChain runtimeChain) {
        this.runtimeChain = runtimeChain;
        return this;
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public RuntimeChain getRuntimeChain() {
        return this.runtimeChain;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/arch/App$DefaultRuntimeScheduler.class */
    public static class DefaultRuntimeScheduler implements RuntimeSchedular {
        Timer timer = new Timer(true);

        private DefaultRuntimeScheduler() {}

        @Override // top.yuumo.meisterhau.lib.ecs.RuntimeSchedular
        public void schedule(final RuntimeChain chain, final App app) {
            chain.getSystemChain(RuntimeLabel.Startup).runWithOnlyReflects(app);
            this.timer.schedule(new TimerTask() { // from class: top.yuumo.meisterhau.lib.arch.App.DefaultRuntimeScheduler.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    chain.scheduleOnce(app);
                }
            }, 0L, 50L);
        }

        @Override // top.yuumo.meisterhau.lib.ecs.RuntimeSchedular
        public void cancel() {
            this.timer.cancel();
        }
    }

    @Override // top.yuumo.meisterhau.lib.arch.SystemManager
    public RuntimeManager removeSystems(String runtimeLabel) {
        for (Method system : getSystems(runtimeLabel)) {
            removeSystem(system, runtimeLabel);
        }
        return this;
    }

    private void requireNoneNull(Object o) throws NullPointerException {
        if (Objects.isNull(o)) {
            throw new NullPointerException("");
        }
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public SystemChain getSystemChain(String runtimeLabel) throws NullPointerException {
        requireNoneNull(this.runtimeChain);
        return this.runtimeChain.getSystemChain(runtimeLabel);
    }

    @Override // top.yuumo.meisterhau.lib.arch.RuntimeManager
    public RuntimeManager setSystemChain(String runtimeLabel, SystemChain systemChain) throws NullPointerException {
        requireNoneNull(this.runtimeChain);
        this.runtimeChain.setSystemChain(runtimeLabel, systemChain);
        return this;
    }

    @Override
    public ComponentManager addSingleComponent(Object obj) {
        if (Objects.nonNull(obj)) {
            singletonComponents.add(obj);
        }
        return this;
    }
}
