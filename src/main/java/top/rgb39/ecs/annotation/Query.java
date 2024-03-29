package top.rgb39.ecs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
    Class<?>[] value();
    Class<?>[] with() default {};
    Class<?>[] without() default {};
}
