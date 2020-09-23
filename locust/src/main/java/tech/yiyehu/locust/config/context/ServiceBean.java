package tech.yiyehu.locust.config.context;

import tech.yiyehu.locust.config.ServiceConfig;

public class ServiceBean extends ServiceConfig {
    Object bean;
    String beanName;
    public String getBeanName() {
        return this.beanName;
    }
}
