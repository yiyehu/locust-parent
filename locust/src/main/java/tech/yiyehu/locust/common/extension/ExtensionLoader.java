package tech.yiyehu.locust.common.extension;

import tech.yiyehu.locust.common.URL;

import java.util.List;

public class ExtensionLoader<T> {

    public List<T> getExtension(String key) {
        return null;
    }

    public List<T> getActivateExtension(URL url, String key) {
        return getActivateExtension(url, key);
    }
}
