package tech.yiyehu.locust.config.annotation;

import org.springframework.context.annotation.Import;
import tech.yiyehu.locust.config.context.LocustComponentScanRegistrar;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LocustComponentScanRegistrar.class)
public @interface LocustComponentScan {
    /**
     * packages
     * @return
     */
    String[] values() default {};
}
