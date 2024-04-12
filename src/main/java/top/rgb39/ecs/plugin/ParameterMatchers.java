package top.rgb39.ecs.plugin;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

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

    private boolean matchQuery(List<Object> args, Parameter param, int index, App app, Long entityId) {
        Query query = (Query) param.getAnnotation(Query.class);
        if (Objects.isNull(query)) {
            return false;
        }

        Class<?>[] value = query.value();
        Class<?>[] with = query.with();
        Class<?>[] without = query.without();

        var all = Arrays.stream(Objects.requireNonNull(app.table).getRowArray(entityId))
            .filter(Objects::nonNull)
            .map(Object::getClass)
            .collect(Collectors.toList());

        Set<Class<?>> shouldExsits = new HashSet<>() {{
            addAll(Arrays.asList(value));
            addAll(Arrays.asList(with));
        }};

        

        for (Class<?> withClass : shouldExsits) {
            if (!all.contains(withClass)) {
                return false;
            }
        }

        for (Class<?> withoutClass : without) {
            if (all.contains(withoutClass)) {
                return false;
            }
        }

        List<Object> components = new Vector<>() {{
            for (Class<?> valueClass : value) {
                add(app.getComponent(entityId, valueClass));
            }
        }};
        
        args.set(index, components);  

        return true;
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
