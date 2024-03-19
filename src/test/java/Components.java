import top.rgb39.ecs.RuntimeLabel;
import top.rgb39.ecs.annotation.Reflect;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.loader.Scanner;
import top.rgb39.ecs.loader.SystemLoader;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.annotation.System;;

public class Components {
    public static void main(String[] args) throws Exception {
        Logger.enableLogger(Logger.ECS);
        App app = App.create();
        Components components = new Components();
        Scanner.classes.put("top.rgb39.ecs.test", Components.class);
        SystemLoader.load(app);

        app.addSingleComponent(components);
        app.run();
    }

    @System(runtimeLabel = RuntimeLabel.Startup)
    public void test(
        @Reflect(Components.class) Components components
    ) {
        Logger.ECS.info(components.toString());
    }
}
