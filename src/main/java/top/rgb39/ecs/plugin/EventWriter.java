package top.rgb39.ecs.plugin;

import top.rgb39.ecs.arch.Event;

public interface EventWriter {
    void write(Event event);
    void clear();
}