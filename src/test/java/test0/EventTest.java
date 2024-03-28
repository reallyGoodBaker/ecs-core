package test0;

import java.util.List;
import top.rgb39.ecs.annotation.Component;
import top.rgb39.ecs.annotation.Entity;
import top.rgb39.ecs.annotation.Read;
import top.rgb39.ecs.annotation.Slot;
import top.rgb39.ecs.annotation.System;
import top.rgb39.ecs.annotation.Write;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Event;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.plugin.EventWriter;
import top.rgb39.ecs.util.Logger;

public class EventTest {
    public static void main(String[] args) {
        Logger.enableLogger(Logger.DEBUG);
        Logger.enableLogger("tick");
        App app = App.create("test0/EventTest.class");

        app.addEntity(0, new State())
            .addSingleComponent(app)
            .run();
    }

    @System(runtimeLabel = RuntimeLabel.Event)
    void test(
        @Write EventWriter writer,
        @Slot(State.class) State s
    ) {
        if (s.count % 100 == 0) {
            writer.write(new MyEvent());
        }
    }

    @System
    void test2(
        @Entity long id,
        @Read(MyEvent.class) List<MyEvent> events
    ) {
        Logger.DEBUG.i("size: " + events.size());
    }

    @System
    void addCount(
        @Slot(State.class) State s
    ) {
        s.count++;
    }
}

@Component
class State {
    int count = 0;
}

class MyEvent implements Event { }