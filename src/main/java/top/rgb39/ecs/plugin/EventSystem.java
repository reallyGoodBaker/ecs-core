package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.executor.SystemConfig;

public class EventSystem implements Plugin {

    @Override
    public void build(App app) {
        try {
            app.addSystem(
                EventSystem.class.getDeclaredMethod("fireEvents"),
                new SystemConfig(RuntimeLabel.AfterEvent, false)
            );
            app.addSystem(
                EventSystem.class.getDeclaredMethod("clearEvents"),
                new SystemConfig(RuntimeLabel.AfterUpdate, false)
            );
        } catch (Exception e) {}
    }

    void fireEvents() {
        Events.fireEvents();
    }

    void clearEvents() {
        Events.clearEvents();
    }

}
