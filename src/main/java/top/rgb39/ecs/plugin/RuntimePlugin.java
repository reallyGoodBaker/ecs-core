package top.rgb39.ecs.plugin;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.RuntimeManager;
import top.rgb39.ecs.executor.RuntimeChain;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.executor.RuntimeSchedular;
import top.rgb39.ecs.executor.SystemChain;

public class RuntimePlugin implements RuntimeManager, Plugin {

    private RuntimeSchedular schedular;
    private RuntimeChain runtimeChain;

    @Override
    public void build(App app) {
        SystemChain beforeUpdate = new SystemChain();
        SystemChain event = new SystemChain();
        SystemChain update = new SystemChain();
        SystemChain afterUpdate = new SystemChain();
        SystemChain startup = new SystemChain();

        runtimeChain = new RuntimeChain() {
            @Override
            public String[] getScheduleSequence() {
                return new String[]{
                    RuntimeLabel.Event,
                    RuntimeLabel.BeforeUpdate,
                    RuntimeLabel.Update,
                    RuntimeLabel.AfterUpdate
                };
            }
        };

        runtimeChain.setSystemChain(RuntimeLabel.Event, event);
        runtimeChain.setSystemChain(RuntimeLabel.BeforeUpdate, beforeUpdate);
        runtimeChain.setSystemChain(RuntimeLabel.Update, update);
        runtimeChain.setSystemChain(RuntimeLabel.AfterUpdate, afterUpdate);
        runtimeChain.setSystemChain(RuntimeLabel.Startup, startup);

        setScheduler(new DefaultRuntimeScheduler());

        app.setRuntimeManager(this);
    }

    @Override
    public RuntimeManager setScheduler(RuntimeSchedular schedular) {
        this.schedular = schedular;
        return this;
    }

    @Override
    public void run(App app) {
        if (Objects.nonNull(this.schedular)) {
            this.schedular.schedule(this.runtimeChain, app);
        }
    }

    @Override
    public void stop() {
        if (Objects.nonNull(this.schedular)) {
            this.schedular.cancel();
        }
    }

    @Override
    public RuntimeManager setRuntimeChain(RuntimeChain runtimeChain) {
        this.runtimeChain = runtimeChain;
        return this;
    }

    @Override
    public RuntimeChain getRuntimeChain() {
        return this.runtimeChain;
    }

    @Override
    public SystemChain getSystemChain(String runtimeLabel) throws NullPointerException {
        Objects.requireNonNull(this.runtimeChain);
        return this.runtimeChain.getSystemChain(runtimeLabel);
    }

    @Override
    public RuntimeManager setSystemChain(String runtimeLabel, SystemChain systemChain) throws NullPointerException {
        Objects.requireNonNull(this.runtimeChain);
        this.runtimeChain.setSystemChain(runtimeLabel, systemChain);
        return this;
    }

    public static class DefaultRuntimeScheduler implements RuntimeSchedular {
        TimerTask task;

        private DefaultRuntimeScheduler() {}

        @Override
        public void schedule(final RuntimeChain chain, final App app) {
            chain.getSystemChain(RuntimeLabel.Startup)
                .runWithOnlyReflects(app);

            new Timer(true).schedule(task = new TimerTask() {
                @Override
                public void run() {
                    chain.scheduleOnce(app);
                }
            }, 0L, 50L);
        }

        @Override
        public void cancel() {
            this.task.cancel();
        }
    }

    @Override
    public String currentRuntimeLabel() {
        return runtimeChain.current();
    }

}
