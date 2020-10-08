package tech.yiyehu.locust.registry.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.registry.Registry;
import tech.yiyehu.locust.registry.RegistryFactory;
import tech.yiyehu.locust.rpc.exception.RpcException;

public class RedisRegistyFactory implements RegistryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRegistyFactory.class);
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

    public static Registry getRegistry() {
        return registry;
    }

    public static void destroyAll() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Close registry " + getRegistry());
        }
        try {
            registry.destroy();
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
