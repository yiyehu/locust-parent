package tech.yiyehu.locust.registry;

import tech.yiyehu.locust.common.URL;

import java.util.List;

public interface NotifyListener {
    /**
     *  notify subscribers when a service change
     * @param urls
     */
    void notify(List<URL> urls);
}
