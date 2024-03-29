package top.rgb39.ecs.executor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Row;
import top.rgb39.ecs.arch.Table;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.util.Logger.FontColors;
import top.rgb39.ecs.annotation.System;

public class SystemChain {
    private List<Method> systems = new ArrayList<>();

    public void runWithOnlyReflects(App app) {
        for (Method method : this.systems) {
            ParameterImplementor implementor = new ParameterImplementor(method.getParameters());
            implementor.matchArgumentsOnlyReflect(app);
            try {
                implementor.invoke(SystenInstanceRecord.getInstance(method.getDeclaringClass()), method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run(App app) {
        for (Method system : this.systems) {
            System sysAnnotaion = (System) system.getAnnotation(System.class);
            if (Objects.isNull(sysAnnotaion) || Objects.isNull(app.table)) {
                return;
            }
            for (Row row : ((Table) Objects.requireNonNull(app.table)).getRows()) {
                ParameterImplementor implementor = new ParameterImplementor(system.getParameters());
                implementor.matchArguments(app, row.getRowId());
                try {
                    if (sysAnnotaion.asynchronous()) {
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                return implementor.invoke(SystenInstanceRecord.getInstance(system.getDeclaringClass()), system);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        });
                    } else {
                        implementor.invoke(SystenInstanceRecord.getInstance(system.getDeclaringClass()), system);
                    }
                    Logger.info("tick", FontColors.GREEN, "system: " + system.getName());
                } catch (Exception e) {
                    Logger.info("tick", FontColors.RED, "system failed: " + system.getName());
                }
            }
        }
    }

    public void addSystem(Method system) {
        this.systems.add(system);
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
