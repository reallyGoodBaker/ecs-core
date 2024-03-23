package top.rgb39.ecs.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import top.rgb39.ecs.arch.App;

public class RuntimeChain {
    private Map<String, SystemChain> sysChains = new HashMap<>();
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

    public CompletableFuture<Void> scheduleOnce(App app) {
        CompletableFuture<?> ftr = CompletableFuture.runAsync(() -> {});
        return _scheduleOnce(getScheduleSequence(), 0, ftr, app);
    }

    private CompletableFuture<Void> _scheduleOnce(String[] scheduleSeq, int index, CompletableFuture<?> lastFuture, App app) {
        return lastFuture.thenRun(() -> {
            currentSchedule = scheduleSeq[index];
            if (index < this.sysChains.size()) {
                SystemChain sysChain = this.sysChains.get(scheduleSeq[index]);
                CompletableFuture<Void> ftr = sysChain.run(app);
                ftr.thenRun(() -> {
                    _scheduleOnce(scheduleSeq, index + 1, ftr, app);
                });
            }
        });
    }

    public String current() {
        return currentSchedule;
    }
}
