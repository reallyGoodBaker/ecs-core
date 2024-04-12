package top.rgb39.ecs.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import top.rgb39.ecs.arch.App;

public class RuntimeChain {
    private Map<String, SystemChain> sysChains = new HashMap<>();

    @Nullable
    private String currentSchedule = null;

    public void setSystemChain(String runtimeLabel, SystemChain sysChain) {
        this.sysChains.put(runtimeLabel, sysChain);
    }

    public SystemChain getSystemChain(String runtimeLabel) {
        return this.sysChains.get(runtimeLabel);
    }

    public void removeSystemChain(String runtimeLabel, SystemChain sysChain) {
        this.sysChains.put(runtimeLabel, sysChain);
    }

    public void clearSystemChains() {
        this.sysChains.clear();
    }

    public String[] getScheduleSequence() {
        return (String[]) this.sysChains.keySet().toArray(new String[0]);
    }

    public CompletableFuture<?> scheduleOnce(App app) {
        return CompletableFuture.runAsync(() -> {
            String[] scheduleSeq = getScheduleSequence();
            var futureList = new CompletableFuture[scheduleSeq.length];

            for (int i = 0; i < scheduleSeq.length; i++) {
                currentSchedule = scheduleSeq[i];
                SystemChain sysChain = this.sysChains.get(scheduleSeq[i]);
                futureList[i] = sysChain.run(app);
            }

            CompletableFuture.allOf(futureList).join();
            currentSchedule = null;
        });
    }

    public String current() {
        return currentSchedule;
    }

    public boolean idle() {
        return currentSchedule == null;
    }
}
