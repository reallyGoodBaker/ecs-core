package top.rgb39.ecs;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import top.rgb39.ecs.arch.App;
import top.rgb39.ecs.arch.Table;
import top.rgb39.ecs.annotation.Reflect;
import top.rgb39.ecs.annotation.Entity;
import top.rgb39.ecs.annotation.Query;
import top.rgb39.ecs.annotation.Slot;

/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/ArgumentImplementor.class */
public class ArgumentImplementor {
    private final Parameter[] params;
    private final List<Object> args;
    private static final Object EMPTY = new Object();

    public ArgumentImplementor(Parameter[] params) {
        this.params = params;
        Object[] _args = new Object[params.length];
        Arrays.fill(_args, EMPTY);
        this.args = Arrays.asList(_args);
    }

    public void set(int index, Object value) {
        this.args.set(index, value);
    }

    public Object get(int index) {
        return this.args.get(index);
    }

    public Object invoke(Object target, Method method) throws Exception {
        if (this.args.contains(EMPTY)) {
            return null;
        }
        return method.invoke(target, this.args.toArray());
    }

    public ArgumentImplementor matchArgumentsOnlyReflect(App app) {
        for (int i = 0; i < this.params.length; i++) {
            Parameter param = this.params[i];
            matchReflects(param, i, app);
        }
        return this;
    }

    public ArgumentImplementor matchArguments(App app, long entityId) {
        int i = 0;
        while (i < this.params.length) {
            Parameter param = this.params[i];
            i = (matchReflects(param, i, app) || matchEntity(param, i, entityId) || matchQuery(param, i, app) || !matchSlot(param, i, app, entityId)) ? i + 1 : i + 1;
        }
        return this;
    }

    public boolean matchReflects(Parameter param, int index, App app) {
        Reflect reflect = (Reflect) param.getAnnotation(Reflect.class);
        
        if (Objects.isNull(reflect)) {
            return false;
        }

        Class<?> targetClass = reflect.value();
        Object target = app.getSingletonComponent(targetClass);

        if (Objects.nonNull(target)) {
            this.args.set(index, target);
            return true;
        }

        return false;
    }

    // TODO
    public boolean matchQuery(Parameter param, int index, App app) {
        Query query = (Query) param.getAnnotation(Query.class);
        if (Objects.nonNull(query)) {
            this.args.set(index, null);
            return true;
        }
        return false;
    }

    public boolean matchEntity(Parameter param, int argIndex, long entityId) {
        Entity entity = (Entity) param.getAnnotation(Entity.class);
        if (Objects.nonNull(entity)) {
            this.args.set(argIndex, Long.valueOf(entityId));
            return true;
        }
        return false;
    }

    public boolean matchSlot(Parameter param, int argIndex, App app, long entityId) {
        int index;
        Slot slot = (Slot) param.getAnnotation(Slot.class);
        if (Objects.isNull(slot)) {
            return false;
        }
        Table table = app.table;
        if (Objects.isNull(table) || (index = ((Table) Objects.requireNonNull(table)).getColumn(slot.value())) == -1) {
            return false;
        }
        Object component = table.getRow(entityId).getCell(index);
        if (Objects.isNull(component)) {
            return false;
        }
        this.args.set(argIndex, component);
        return true;
    }
}
