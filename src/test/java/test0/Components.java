package test0;
import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.annotation.Reflect;
import top.rgb39.ecs.annotation.Slot;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.annotation.System;;

public class Components {
    public static void main(String[] args) throws Exception {
        Logger.enableLogger(Logger.ECS);
        App.create("test0")
            .addSingleComponent(new Components())
            .addEntity(0, new Counter())
            .run();
    }

    @System(runtimeLabel = RuntimeLabel.Startup)
    private void test(
        @Reflect(Components.class) Components components
    ) {
        Logger.ECS.i(components.toString());
    }

    @System
    private void updateCounter(
        @Slot(Counter.class) Counter counter
    ) {
        counter.i++;
        java.lang.System.out.println(counter.i);
    }
}

@Component
class Counter {
    int i = 0;
}