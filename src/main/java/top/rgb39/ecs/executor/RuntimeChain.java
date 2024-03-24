package top.rgb39.ecs.executor;

import java.util.HashMap;
import java.util.Map;
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

    public void scheduleOnce(App app) {
        Logger.info("tick",FontColors.CYAN,  "schedule start");
        
        int index = 0;
        String[] scheduleSeq = getScheduleSequence();
        while (index < scheduleSeq.length) {
            currentSchedule = scheduleSeq[index];
            SystemChain sysChain = this.sysChains.get(scheduleSeq[index]);
            sysChain.run(app);
            index++;
        }

        Logger.info("tick",FontColors.YELLOW,  "schedule end");
    }

    public String current() {
        return currentSchedule;
    }
}
