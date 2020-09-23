package tech.yiyehu.locust.registry.redis;


import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.registry.Registry;
import tech.yiyehu.locust.registry.RegistryFactory;
import tech.yiyehu.locust.rpc.exception.RpcException;

public class RedisRegistyFactory implements RegistryFactory {
    private static Registry registry;

    @Override
    public Registry getRegistry(URL url) throws RpcException {
        if (registry == null) {
            synchronized (this) {
                registry = new RedisRegistry(url);
            }
        }
        return registry;
    }
}
