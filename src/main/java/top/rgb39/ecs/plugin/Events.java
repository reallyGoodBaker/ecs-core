package top.rgb39.ecs.plugin;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import top.rgb39.ecs.annotation.Read;
import top.rgb39.ecs.annotation.Write;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Event;
import top.rgb39.ecs.executor.ParameterImplementor;
import top.rgb39.ecs.executor.ParameterMatcher;
import top.rgb39.ecs.executor.RuntimeLabel;
import top.rgb39.ecs.executor.SystemConfig;
import top.rgb39.ecs.executor.SystemInstanceRecord;

public class Events implements Plugin, ParameterMatcher {

    static final List<Event> events = new Vector<>();
    static final List<Event> tempEvents = new Vector<>();

    @Override
    public void build(App app) {
        ParameterImplementor.registerMatcher(this);
        SystemInstanceRecord.setInst(Events.class, this);
        try {
            app.addSystem(
                Events.class.getDeclaredMethod("fireEvents"),
                new SystemConfig(RuntimeLabel.AfterEvent, false)
            );
            app.addSystem(
                Events.class.getDeclaredMethod("clearEvents"),
                new SystemConfig(RuntimeLabel.AfterUpdate, false)
            );
        } catch (Exception e) {}
    }

    @Override
    public boolean match(List<Object> args, Parameter param, int argIndex, App app, @Nullable Long entityId) {
        Read reader;

        if (Objects.nonNull(reader = param.getAnnotation(Read.class))) {
            return matchReader(args, reader, argIndex, app);
        }

        if (Objects.nonNull(param.getAnnotation(Write.class))) {
            return matchWriter(args, argIndex, app);
        }

        return false;
    }

    private boolean matchReader(List<Object> args, Read reader, int argIndex, App app) {
        if (app.getRuntimeManager().isRuntimeLabel(RuntimeLabel.AfterUpdate)) {
            return false;
        }

        Class<?> ev = reader.value();

        if (Event.class.equals(ev)) {
            Stream<Object> evs = Arrays.stream(events.toArray());

            args.set(argIndex, evs);
            return true;
        }

        Stream<Object> evs = Arrays.stream(events.toArray())
                .filter(e -> e.getClass().equals(ev));

        args.set(argIndex, evs);
        return true;
    }

    private boolean matchWriter(List<Object> args, int argIndex, App app) {
        if (!app.getRuntimeManager().isRuntimeLabel(RuntimeLabel.Event)) {
            return false;
        }

        EventWriterImpl writer = new EventWriterImpl(tempEvents);
        args.set(argIndex, writer);
        return true;
    }

    static void fireEvents() {
        if (tempEvents.size() > 0) {
            events.addAll(tempEvents);
        }

        tempEvents.clear();
    }

    static void clearEvents() {
        events.clear();
    }

    static record EventWriterImpl(List<Event> fragments) implements EventWriter {

        @Override
        public void write(Event event) {
            fragments.add(event);
        }

    }
}
