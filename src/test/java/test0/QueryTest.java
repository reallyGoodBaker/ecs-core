package test0;

import java.util.List;

import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.annotation.Query;
import top.rgb39.ecs.annotation.Reflect;
import top.rgb39.ecs.annotation.System;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.util.Logger;

public class QueryTest {
    public static void main(String[] args) {
        Logger.enableLogger(Logger.DEBUG);
        Logger.enableLogger(Logger.ECS);
        App.create("test0/QueryTest.class")
            .run();
    }

    @System(runtimeLabel = RuntimeLabel.Startup)
    void startup(
        @Reflect(App.class) App app
    ) {
        app.addEntity(0, new A(), new B(), new C());
        app.addEntity(1, new B(), new C());
        app.addEntity(2, new A(), new B());
        Logger.DEBUG.i("start up");
    }

    @System
    void selectWithoutC(
        @Query(
            value = {A.class},
            without = {C.class}) List<Object> res
    ) {
        Logger.DEBUG.i("without c: " + res.size());
    }

    @System
    void withBWithoutC(
        @Query(
            value = {A.class},
            with = {B.class},
            without = {C.class}) List<Object> res
    ) {
        Logger.DEBUG.i("with B without C: " + res.size());
    }
}

@Component
class A {

}

@Component
class B {
 
}

@Component
class C {
 
}