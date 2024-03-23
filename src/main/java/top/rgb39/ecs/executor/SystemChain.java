package top.rgb39.ecs.executor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Row;
import top.rgb39.ecs.arch.Table;
import top.rgb39.ecs.annotation.System;

public class SystemChain {
    private boolean aborted;
    private List<Method> systems = new ArrayList<>();
    private int index = 0;
    private int completed = 0;

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

    public void next(App app) {
        Object invoke;
        if (this.index < 0 || this.index >= this.systems.size()) {
            this.aborted = true;
        }
        if (this.aborted) {
            return;
        }
        List<Method> list = this.systems;
        int i = this.index;
        this.index = i + 1;
        Method system = list.get(i);
        System sysAnnotaion = (System) system.getAnnotation(System.class);
        if (Objects.isNull(sysAnnotaion) || Objects.isNull(app.table)) {
            return;
        }
        for (Row row : ((Table) Objects.requireNonNull(app.table)).getRows()) {
            Object rest = null;
            ParameterImplementor implementor = new ParameterImplementor(system.getParameters());
            implementor.matchArguments(app, row.getRowId());
            try {
                if (sysAnnotaion.asynchronous()) {
                    invoke = CompletableFuture.supplyAsync(() -> {
                        try {
                            return implementor.invoke(SystenInstanceRecord.getInstance(system.getDeclaringClass()), system);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
                } else {
                    invoke = implementor.invoke(SystenInstanceRecord.getInstance(system.getDeclaringClass()), system);
                }
                rest = invoke;
            } catch (Exception e) {
                next(app);
            }
            if (rest instanceof CompletableFuture) {
                this.completed++;
                ((CompletableFuture<?>) rest).thenRun(() -> {
                    this.completed--;
                });
            }
        }
    }

    public void stop() {
        this.aborted = true;
    }

    public CompletableFuture<Void> run(App app) {
        return CompletableFuture.runAsync(() -> {
            while (!isFinished()) {
                next(app);
            }
            restore();
        });
    }

    public boolean isAborted() {
        return this.aborted;
    }

    public boolean isCompleted() {
        return this.completed == 0 && this.index == this.systems.size();
    }

    public boolean isFinished() {
        return isAborted() || isCompleted();
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

    public void restore() {
        this.index = 0;
        this.aborted = false;
        this.completed = 0;
    }
}
