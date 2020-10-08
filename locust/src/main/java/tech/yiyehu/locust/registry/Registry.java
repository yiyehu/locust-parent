package tech.yiyehu.locust.registry;

import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.rpc.exception.RpcException;

import java.util.List;

public interface Registry {
    String REGISTRY_EVENT = "REGISTRY_EVENT";
    String UNREGISTRY_EVENT = "UNREGISTRY_EVENT";

    void register(URL url) throws RpcException;

    void unregister(URL url) throws RpcException;

    void subscribe(URL url, NotifyListener listener) throws RpcException;

    void unsubscribe(URL url, NotifyListener listener);

    /**
     * Query a service with url
     *
     * @param url
     * @return
     */
    List<URL> lookup(URL url);
}
