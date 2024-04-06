package top.rgb39.ecs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.rgb39.ecs.executor.RuntimeLabel;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface System {
    String runtimeLabel() default RuntimeLabel.Update;
    boolean asynchronous() default false;
}
