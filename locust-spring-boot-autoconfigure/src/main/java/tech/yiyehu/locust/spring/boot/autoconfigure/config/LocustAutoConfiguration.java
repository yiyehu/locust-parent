package tech.yiyehu.locust.spring.boot.autoconfigure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.yiyehu.locust.config.*;

@Configuration
public class LocustAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("locust.application")
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("locust.provider")
    @ConditionalOnProperty(prefix = "locust",name = "type",havingValue = "provider")
    public ProviderConfig providerConfig() {
        return new ProviderConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("locust.consumer")
    @ConditionalOnProperty(prefix = "locust",name = "type",havingValue = "consumer")
    public ConsumerConfig consumerConfig() {
        return new ConsumerConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("locust.registry")
    public RegistryConfig registryConfig() {
        return new RegistryConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("locust.protocol")
    public ProtocolConfig protocolConfig() {
        return new ProtocolConfig();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("locust.monitor")
    public MonitorConfig monitorConfig() {
        return new MonitorConfig("monitor");
    }
}
