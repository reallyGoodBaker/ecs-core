package top.rgb39.ecs.executor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Row;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.util.Logger.FontColors;
import top.rgb39.ecs.annotation.System;
import top.rgb39.ecs.util.Option;

public class SystemChain {
    private final List<Method> systems = new ArrayList<>();

    public void runWithOnlyReflects(App app) {
        for (Method method : this.systems) {
            ParameterImplementor implementor = new ParameterImplementor(method.getParameters());
            implementor.matchArgumentsOnlyReflect(app);
            try {
                implementor.invoke(SystemInstanceRecord.getInstance(method.getDeclaringClass()), method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CompletableFuture<?> run(App app) {
        var futureList = new ArrayList<CompletableFuture<?>>();

        for (Method system : this.systems) {
            var systemConfig = Option.it(SystemConfig.T);

            if (Objects.requireNonNull(SystemRecord.getConfig(system).state(systemConfig)) == Option.Opt.NONE) {
                var it = Option.it(SystemConfig.T);

                switch (SystemConfig.from(system.getAnnotation(System.class)).state(it)) {
                    case NONE -> {
                        continue;
                    }
                    case SOME -> SystemRecord.record(system, it.v());
                }

                systemConfig = it;
            }
            
            for (Row row : app.table.getRows()) {
                ParameterImplementor implementor = new ParameterImplementor(system.getParameters());
                implementor.matchArguments(app, row.getRowId());
                try {
                    if (systemConfig.v().asynchronous()) {
                        var future = CompletableFuture.supplyAsync(() -> {
                            try {
                                return implementor.invoke(SystemInstanceRecord.getInstance(system.getDeclaringClass()), system);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        });

                        futureList.add(future);
                    } else {
                        implementor.invoke(SystemInstanceRecord.getInstance(system.getDeclaringClass()), system);
                    }
                } catch (Exception e) {
                    Logger.info("tick", FontColors.RED, "system failed: " + system.getName());
                }
            }
        }

        return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
    }

    public void addSystem(Method system) {
        this.systems.add(system);
    }

    public void addSystem(Method system, SystemConfig sysConfig) {
        this.systems.add(system);
        SystemRecord.record(system, sysConfig);
    }

    public void removeSystem(Method system) {
        this.systems.remove(system);
    }

    public boolean replaceSystem(Method system, Method old) {
        int index = this.systems.indexOf(old);
        if (index < 0 || index >= this.systems.size()) {
            return false;
        }
        this.systems.set(index, system);
        return true;
    }

    public void clearSystems() {
        this.systems.clear();
    }

    public Method[] getSystems() {
        return (Method[]) this.systems.toArray(new Method[0]);
    }

}
