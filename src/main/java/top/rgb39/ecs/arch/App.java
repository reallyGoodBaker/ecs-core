package top.rgb39.ecs.arch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.component.ComponentFactory;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.executor.SystemChain;
import top.rgb39.ecs.plugin.DefaultPlugins;
import top.rgb39.ecs.plugin.Plugin;
import top.rgb39.ecs.util.Lists;

public class App implements
    EntityManager, ComponentManager, ExecutorManager, SystemManager, PluginManager
{
    @Nullable
    public final Table table = new Table();
    public final List<Object> singletonComponents = new ArrayList<>();
    private final Map<Class<?>, Plugin> plugins = new HashMap<>();
    private RuntimeManager runtimeManager;

    @Override
    public App addEntity(long entityId, Object... components) {
        for (Object obj : components) {
            ((Table) Objects.requireNonNull(this.table)).setCell(obj.getClass(), entityId, obj);
        }
        return this;
    }

    @Override
    public App removeEntity(long entityId) {
        ((Table) Objects.requireNonNull(this.table)).deleteRow(entityId);
        return this;
    }

    @Override
    public Object[] getComponents(long entityId) {
        return ((Table) Objects.requireNonNull(this.table)).getRowArray(entityId);
    }

    @Override
    public Object[] getSingletonComponents() {
        return singletonComponents.toArray();
    }

    @Override
    public Object getComponent(long entityId, Class<?> componentClass) {
        Object component = ((Table) Objects.requireNonNull(this.table)).getCell(componentClass, entityId);
        if (Objects.nonNull(component)) {
            return component;
        }
        Object component2 = ComponentFactory.getComponent(componentClass);
        ((Table) Objects.requireNonNull(this.table)).setCell(componentClass, entityId, component2);
        return component2;
    }

    @Override
    public Object getSingletonComponent(Class<?> componentClass) {
        Object component = Lists.find(singletonComponents, c -> {
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

    @Override
    public App addComponent(long entityId, Object component) {
        Component c = component.getClass().getAnnotation(Component.class);
        if (Objects.nonNull(c) && c.singleton()) {
            addSingleComponent(component);
        }
        ((Table) Objects.requireNonNull(this.table)).setCell(component.getClass(), entityId, component);
        return this;
    }

    @Override
    public App removeComponent(long entityId, Class<?> componentClass) {
        ((Table) Objects.requireNonNull(this.table)).deleteCell(componentClass, entityId);
        return this;
    }

    @Override
    public App removeSingletonComponent(Class<?> componentClass) {
        Object component = Lists.find(this.singletonComponents, c -> {
            return c.getClass().equals(componentClass);
        });
        if (Objects.nonNull(component)) {
            singletonComponents.remove(component);
        }
        return this;
    }


    @Override
    public App addSystem(Method system, String label) {
        system.setAccessible(true);
        SystemChain chain = runtimeManager.getSystemChain(label);
        if (Objects.nonNull(chain)) {
            chain.addSystem(system);
        }
        return this;
    }

    @Override
    public App addSystem(Method system) {
        return addSystem(system, RuntimeLabel.Update);
    }

    @Override
    public App removeSystem(Method system, String label) {
        SystemChain chain = runtimeManager.getSystemChain(label);
        if (Objects.nonNull(chain)) {
            chain.removeSystem(system);
        }
        return this;
    }

    @Override
    public App removeSystem(Method system) {
        return removeSystem(system, RuntimeLabel.Update);
    }

    @Override
    public boolean replaceSystem(Method system, Method old, String label) {
        SystemChain chain = runtimeManager.getSystemChain(label);
        if (Objects.nonNull(chain)) {
            chain.replaceSystem(system, old);
            return true;
        }
        return false;
    }

    @Override
    public boolean replaceSystem(Method system, Method old) {
        return replaceSystem(system, old, RuntimeLabel.Update);
    }

    @Override
    public Method[] getSystems() {
        return getSystems(RuntimeLabel.Update);
    }

    @Override
    public Method[] getSystems(String label) {
        SystemChain chain = runtimeManager.getSystemChain(label);
        if (Objects.isNull(chain)) {
            return new Method[0];
        }
        return chain.getSystems();
    }

    public static App empty() {
        return new App();
    }

    public static App create(String... names) {
        App app = new App();
        app.addPlugins(new DefaultPlugins(names));
        return app;
    }

    @Override
    public App removeSystems(String runtimeLabel) {
        for (Method system : getSystems(runtimeLabel)) {
            removeSystem(system, runtimeLabel);
        }
        return this;
    }

    @Override
    public App addSingleComponent(Object obj) {
        if (Objects.nonNull(obj)) {
            singletonComponents.add(obj);
        }
        return this;
    }

    private void _addPlugin(Plugin plugin) {
        try {
            plugin.build(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        plugins.put(plugin.getClass(), plugin);
    }

    @Override
    public App addPlugin(Plugin plugin) {
        _addPlugin(plugin);
        return this;
    }

    @Override
    public App addPlugins(Plugin... plugins) {
        for (Plugin plugin : plugins) {
            _addPlugin(plugin);
        }

        return this;
    }

    @Override
    public boolean pluginsReady() {
        return plugins.values()
            .stream()
            .filter(plugin -> !plugin.ready())
            .count() < 1l;
    }

    @Override
    public void setRuntimeManager(RuntimeManager manager) {
        runtimeManager = manager;
    }

    @Override
    public RuntimeManager getRuntimeManager() {
        return runtimeManager;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        App self = this;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pluginsReady()) {
                    runtimeManager.run(self);
                    timer.cancel();
                }
            }
        }, 0l, 500l);
    }

    @Override
    public void stop() {
        runtimeManager.stop();
    }

}
