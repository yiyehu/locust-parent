package tech.yiyehu.locust.config.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Reference {

    /**
     * unique identity mark
     */
    String name() default "";

    /**
     * if name is null ,unique identity mark
     */
    String interfaceName() default "";

    /**
     * if interfaceName is null , interfaceClass.getName is unique identity mark
     */
    Class<?> interfaceClass() default void.class;

    String version() default "";

    String url() default "";

    String port() default "";

    String host() default "";

    /**
     * mapping path
     */
    String httpMapping() default "";

    String type() default "";
}
