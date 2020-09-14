package tech.yiyehu.locust.config;

import tech.yiyehu.locust.config.base.AbstractConfig;

public class ProviderConfig extends AbstractConfig {

    private String host;

    private Integer port;
    // protocol codec
    private String codec;

    private String status;

    private Integer wait;

    private Boolean isDefault;
}
