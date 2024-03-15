import top.rgb39.ecs.annotation.Reflect;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.loader.InternalScanner;
import top.rgb39.ecs.loader.SystemLoader;
import top.rgb39.ecs.util.Logger;

public class Components {
    public static void main(String[] args) throws Exception {
        App app = App.create();
        Components components = new Components();
        InternalScanner.scan("top.rgb39.ecs");
        SystemLoader.load(app);
        Logger.enableDebugger(Logger.ECS);
        app.addSingleComponent(components);

        app.run();

        if (!app.getSingletonComponent(Components.class).equals(components)) {
            throw new Exception("Components not equal");
        }

        System.out.printf("done %s\n", app.getSingletonComponent(Components.class).equals(components));
    }

    @top.rgb39.ecs.annotation.System
    public void test(
        @Reflect(Components.class) Components components
    ) {
        Logger.ECS.info(components.toString());
    }
}
