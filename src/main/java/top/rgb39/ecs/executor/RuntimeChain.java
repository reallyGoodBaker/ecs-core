package top.rgb39.ecs.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.util.Logger.FontColors;

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
        Logger.info("tick",FontColors.CYAN,  "schedule start");
        CompletableFuture<?> ftr = CompletableFuture.runAsync(() -> {});
        CompletableFuture<Void> future = _scheduleOnce(getScheduleSequence(), 0, ftr, app).thenRun(() -> {
            Logger.info("tick",FontColors.YELLOW,  "schedule end");
        });
        return future;
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
            } else {
                lastFuture.complete(null);
            }
        });
    }

    public String current() {
        return currentSchedule;
    }
}
