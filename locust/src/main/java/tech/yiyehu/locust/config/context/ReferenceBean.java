package tech.yiyehu.locust.config.context;

import tech.yiyehu.locust.config.ReferenceConfig;

public class ReferenceBean extends ReferenceConfig {
    Object proxy;
    public Object get(){
        return proxy;
    }
}
