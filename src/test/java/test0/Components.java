package test0;
import top.rgb39.ecs.annotation.Reflect;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.annotation.System;;

public class Components {
    public static void main(String[] args) throws Exception {
        Logger.enableLogger(Logger.ECS);
        App app = App.create("test0")
            .addSingleComponent(new Components());

        app.run();
    }

    @System(runtimeLabel = RuntimeLabel.Startup)
    public void test(
        @Reflect(Components.class) Components components
    ) {
        Logger.ECS.info(components.toString());
    }
}
