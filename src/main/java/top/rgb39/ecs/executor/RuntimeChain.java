package top.rgb39.ecs.executor;

import java.util.HashMap;
import java.util.Map;
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

    public void scheduleOnce(App app) {
        String[] scheduleSeq = getScheduleSequence();
        for (int i = 0; i < scheduleSeq.length; i++) {
            currentSchedule = scheduleSeq[i];
            SystemChain sysChain = this.sysChains.get(scheduleSeq[i]);
            sysChain.run(app);
        }
    }

    public String current() {
        return currentSchedule;
    }
}
