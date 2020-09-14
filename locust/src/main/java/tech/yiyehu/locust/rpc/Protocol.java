package tech.yiyehu.locust.rpc;

import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.common.extension.SPI;
import tech.yiyehu.locust.rpc.exception.RpcException;

@SPI("http")
public interface Protocol {
    /**
     * Get default port when user doesn't config the port.
     *
     * @return default port
     */
    int getDefaultPort();

    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException;

    void destroy();
}
