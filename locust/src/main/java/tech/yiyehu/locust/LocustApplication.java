package tech.yiyehu.locust;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.yiyehu.locust.config.ApplicationConfig;

public class LocustApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("tech.yiyehu.locust");
        context.refresh();
        ApplicationConfig config = (ApplicationConfig) context.getBean("applicationConfig");
        config.hashCode();
    }
}
