package tech.yiyehu.locust.config;

import tech.yiyehu.locust.config.base.AbstractConfig;

public class ReferenceConfig<T> extends AbstractConfig {
    T proxy;
    public T get(){
        return proxy;
    }
}
