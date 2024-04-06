package top.rgb39.ecs.plugin;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import top.rgb39.ecs.annotation.Read;
import top.rgb39.ecs.annotation.Write;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Event;
import top.rgb39.ecs.executor.ParameterImplementor;
import top.rgb39.ecs.executor.ParameterMatcher;

public class Events implements Plugin, ParameterMatcher {

    private final List<Event> events = new Vector<>();

    @Override
    public void build(App app) {
        ParameterImplementor.registerMatcher(this);
    }

    @Override
    public boolean match(List<Object> args, Parameter param, int argIndex, App app, @Nullable Long entityId) {
        Read reader;

        if (Objects.nonNull(reader = param.getAnnotation(Read.class))) {
            matchReader(args, reader, argIndex);
            return true;
        }

        if (Objects.nonNull(param.getAnnotation(Write.class))) {
            matchWriter(args, argIndex, app);
            return true;
        }

        return false;
    }

    private void matchReader(List<Object> args, Read reader, int argIndex) {
        Class<?> ev = reader.value();

        if (Event.class.equals(ev)) {
            List<Event> evs = new ArrayList<>();
            Collections.copy(evs, events);

            args.set(argIndex, evs);
            return;
        }

        List<Event> evs = events.stream()
                .filter(e -> e.getClass().equals(ev))
                .collect(Collectors.toList());

        args.set(argIndex, evs);
    }

    private boolean matchWriter(List<Object> args, int argIndex, App app) {
        EventWriter writer = new EventWriterImpl(events);
        args.set(argIndex, writer);
        return true;
    }

    private record EventWriterImpl(List<Event> events) implements EventWriter {

        @Override
        public void write(Event event) {
            events.add(event);
        }

        @Override
        public void clear() {
            events.clear();
        }
    }
}
