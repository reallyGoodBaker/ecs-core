package top.rgb39.ecs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.rgb39.ecs.RuntimeLabel;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: meisterhau-lib.jar:top/yuumo/meisterhau/lib/ecs/annotation/System.class */
public @interface System {
    String runtimeLabel() default RuntimeLabel.Update;
    boolean asynchronous() default true;
}
