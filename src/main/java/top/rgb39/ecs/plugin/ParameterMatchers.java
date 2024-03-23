package top.rgb39.ecs.plugin;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import top.rgb39.ecs.annotation.Entity;
import top.rgb39.ecs.annotation.Query;
import top.rgb39.ecs.annotation.Slot;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.executor.ParameterImplementor;

public class ParameterMatchers implements Plugin {

    @Override
    public void build(App app) {
        ParameterImplementor.registerMatchers(
            this::matchEntity,
            this::matchSlot,
            this::matchQuery
        );
    }

    // TODO
    private boolean matchQuery(List<Object> args, Parameter param, int index, App app, @Nullable Long entityId) {
        Query query = (Query) param.getAnnotation(Query.class);
        if (Objects.nonNull(query)) {
            args.set(index, null);
            return true;
        }
        return false;
    }

    private boolean matchEntity(List<Object> args, Parameter param, int argIndex, App app, Long entityId) {
        Entity entity = (Entity) param.getAnnotation(Entity.class);
        if (Objects.nonNull(entity)) {
            args.set(argIndex, Long.valueOf(entityId));
            return true;
        }
        return false;
    }

    private boolean matchSlot(List<Object> args, Parameter param, int argIndex, App app, Long entityId) {
        Slot slot = (Slot) param.getAnnotation(Slot.class);
        if (Objects.isNull(slot)) {
            return false;
        }
        Object component = app.getComponent(entityId, slot.value());
        if (Objects.isNull(component)) {
            return false;
        }
        args.set(argIndex, component);
        return true;
    }

}
