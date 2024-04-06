package top.rgb39.ecs.plugin;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.annotation.Nullable;

import top.rgb39.ecs.annotation.Read;
import top.rgb39.ecs.annotation.System;
import top.rgb39.ecs.annotation.Write;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Event;
import top.rgb39.ecs.executor.ParameterImplementor;
import top.rgb39.ecs.executor.ParameterMatcher;
import top.rgb39.ecs.executor.RuntimeLabel;

public class Events implements Plugin, ParameterMatcher {

    private static final List<Event> events = new Vector<>();
    private static final List<Event> tempEvents = new Vector<>();

    @Override
    public void build(App app) {
        ParameterImplementor.registerMatcher(this);
    }

    @Override
    public boolean match(List<Object> args, Parameter param, int argIndex, App app, @Nullable Long entityId) {
        Read reader;

        if (Objects.nonNull(reader = param.getAnnotation(Read.class))) {
            return matchReader(args, reader, argIndex, app);
        }

        if (Objects.nonNull(param.getAnnotation(Write.class))) {
            matchWriter(args, argIndex, app);
            return true;
        }

        return false;
    }

    private boolean matchReader(List<Object> args, Read reader, int argIndex, App app) {
        if (app.getRuntimeManager().isRuntimeLabel(RuntimeLabel.AfterUpdate)) {
            return false;
        }

        Class<?> ev = reader.value();

        if (Event.class.equals(ev)) {
            Event[] evs = events.toArray(new Event[0]);

            args.set(argIndex, evs);
            return true;
        }

        Object[] evs = Arrays.stream(events.toArray(new Event[0]))
                .filter(e -> e.getClass().equals(ev)).toArray();

        args.set(argIndex, evs);
        return true;
    }

    private boolean matchWriter(List<Object> args, int argIndex, App app) {
        if (!app.getRuntimeManager().isRuntimeLabel(RuntimeLabel.Event)) {
            return false;
        }

        EventWriterImpl writer = new EventWriterImpl();
        args.set(argIndex, writer);
        tempEvents.addAll(writer.fragments);
        return true;
    }

    static class EventWriterImpl implements EventWriter {
        final List<Event> fragments = new Vector<>();

        @Override
        public void write(Event event) {
            fragments.add(event);
        }

    }

    public static class EventSystems {

        @System(runtimeLabel = RuntimeLabel.AfterEvent)
        void copyEvents() {
            Collections.copy(events, tempEvents);
        }

        @System(runtimeLabel = RuntimeLabel.AfterUpdate)
        void clearEvents() {
            tempEvents.clear();
            events.clear();
        }
    }
}
