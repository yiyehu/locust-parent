package tech.yiyehu.locust.config.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Service {

    Class<?> interfaceClass() default void.class;

    String interfaceName() default "";

    String version() default "";
}
