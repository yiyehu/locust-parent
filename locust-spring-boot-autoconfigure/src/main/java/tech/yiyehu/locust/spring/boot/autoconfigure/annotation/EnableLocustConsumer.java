package tech.yiyehu.locust.spring.boot.autoconfigure.annotation;

import java.lang.annotation.*;

/**
 * 消费者
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLocustConsumer {

}
