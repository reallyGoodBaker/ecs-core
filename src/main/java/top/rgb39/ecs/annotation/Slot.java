package top.rgb39.ecs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/annotation/Slot.class */
public @interface Slot {
    Class<?> value();
}
