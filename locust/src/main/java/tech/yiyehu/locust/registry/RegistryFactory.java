package tech.yiyehu.locust.registry;

import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.common.extension.SPI;
import tech.yiyehu.locust.rpc.exception.RpcException;

@SPI("redis")
public interface RegistryFactory {

    Registry getRegistry(URL url) throws RpcException;
}
