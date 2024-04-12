package test0;

import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.util.Logger;
import top.rgb39.ecs.util.Option;

public class OptionTest {
    public static void main(String[] args) {
        Logger.enableLogger(Logger.DEBUG);
        Logger.enableLogger(Logger.ECS);
        App.create("test0/OptionTest.class")
            .run();

        testValue(null);
        testValue("Hello");
        testValue(100);
    }

    static void testValue(Object v) {
        var data = (String) Option.some(v)
            .match((NONE, SOME) -> {
                NONE.then(() -> "NONE");
                SOME.then(value -> "SOME: " + value);
            });

        System.out.println(data);
    }
}
