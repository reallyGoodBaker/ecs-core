package top.rgb39.ecs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/annotation/Component.class */
public @interface Component {
    boolean singleton() default false;
}
