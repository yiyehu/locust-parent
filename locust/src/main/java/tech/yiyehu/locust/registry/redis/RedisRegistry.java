package tech.yiyehu.locust.registry.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import tech.yiyehu.locust.common.Constants;
import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.registry.AbstractRegistry;
import tech.yiyehu.locust.registry.NotifyListener;
import tech.yiyehu.locust.rpc.exception.RpcException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RedisRegistry extends AbstractRegistry {

    private Logger logger = LoggerFactory.getLogger(RedisRegistry.class);

    private static final int DEFAULT_REDIS_PORT = 6379;

    //单注册中心
    private JedisPool jedisPool;
    //service subscriber
    private final Map<String, EventSubscriber> subscribeThreads = new ConcurrentHashMap();

    private int reconnectPeriod;

    private int expirePeriod;

    public RedisRegistry(String host, int port) throws RpcException {
        new RedisRegistry(host, port, Constants.DEFAULT_REGISTRY_RECONNECT_PERIOD, Constants.DEFAULT_SESSION_TIMEOUT);
    }

    public RedisRegistry(String host, int port, int reconnectPeriod, int expirePeriod) throws RpcException {
        this.reconnectPeriod = reconnectPeriod;
        this.expirePeriod = expirePeriod;
        Map<String, String> parameters = new HashMap<>(1);
        parameters.put(Constants.REGISTRY_RECONNECT_PERIOD_KEY, String.valueOf(reconnectPeriod));
        parameters.put(Constants.SESSION_TIMEOUT_KEY, String.valueOf(expirePeriod));
        URL url = new URL(Constants.REGISTRY_PROTOCOL, host, port, parameters);
        url.setAddress(host + Constants.COLON_SEPARATOR + port);
        new RedisRegistry(url);
    }

    public RedisRegistry(URL url) throws RpcException {

        super(url);
        this.reconnectPeriod = url.getParameter(Constants.REGISTRY_RECONNECT_PERIOD_KEY, Constants.DEFAULT_REGISTRY_RECONNECT_PERIOD);

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setTestOnBorrow(url.getParameter("test.on.borrow", true));
        config.setTestOnReturn(url.getParameter("test.on.return", false));
        config.setTestWhileIdle(url.getParameter("test.while.idle", false));
        if (url.getParameter("max.idle", 0) > 0)
            config.setMaxIdle(url.getParameter("max.idle", 0));
        if (url.getParameter("min.idle", 0) > 0)
            config.setMinIdle(url.getParameter("min.idle", 0));
        if (url.getParameter("max.active", 0) > 0)
            config.setMaxTotal(url.getParameter("max.active", 0));
        if (url.getParameter("max.total", 0) > 0)
            config.setMaxTotal(url.getParameter("max.total", 0));
        if (url.getParameter("max.wait", url.getParameter("timeout", 0)) > 0)
            config.setMaxWaitMillis(url.getParameter("max.wait", url.getParameter("timeout", 0)));
        if (url.getParameter("num.tests.per.eviction.run", 0) > 0)
            config.setNumTestsPerEvictionRun(url.getParameter("num.tests.per.eviction.run", 0));
        if (url.getParameter("time.between.eviction.runs.millis", 0) > 0)
            config.setTimeBetweenEvictionRunsMillis(url.getParameter("time.between.eviction.runs.millis", 0));
        if (url.getParameter("min.evictable.idle.time.millis", 0) > 0)
            config.setMinEvictableIdleTimeMillis(url.getParameter("min.evictable.idle.time.millis", 0));

        String address = url.getAddress();
        int i = address.indexOf(':');
        String host;
        int port;
        if (i > 0) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = DEFAULT_REDIS_PORT;
        }
        JedisPool jedisPool = new JedisPool(config, host, port,
                url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT), StringUtils.isEmpty(url.getPassword()) ? null : url.getPassword(),
                url.getParameter("db.index", 0));
        this.jedisPool = jedisPool;
//        subscribeThreads.put(address, new EventSubscriber(jedisPool,toServicePath(url)));
    }

    private String toServicePath(URL url) {
        return url.getServiceInterface();
    }

    private String toCategoryPath(URL url) {
        return toServicePath(url) + Constants.PATH_SEPARATOR + url.getParameter(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY);
    }

    @Override
    public void register(URL url) throws RpcException {
        String key = toCategoryPath(url);
        if (StringUtils.isEmpty(key)) {
            logger.warn("");
            return;
        }
        String value = url.toFullString();
        String expire = String.valueOf(System.currentTimeMillis() + expirePeriod);
        boolean success = false;
        RpcException exception = null;
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.hset(key, value, expire);
                jedis.publish(key, Constants.REGISTER);
                success = true;
            } finally {
                jedis.close();
            }
        } catch (Throwable t) {
            exception = new RpcException("Failed to register service to redis registry. registry: " + registryUrl.getAddress() + ", service: " + url + ", cause: " + t.getMessage(), t);
        }
        if (exception != null) {
            if (success) {
                logger.warn(exception.getMessage(), exception);
            } else {
                throw exception;
            }
        }
    }

    @Override
    public void unregister(URL url) throws RpcException {
        String key = toCategoryPath(url);
        if (StringUtils.isEmpty(key)) {
            logger.warn("");
            return;
        }
        String value = url.toFullString();
        boolean success = false;
        RpcException exception = null;
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.hdel(key, value);
                jedis.publish(key, Constants.UNREGISTER);
                success = true;
            } finally {
                jedis.close();
            }
        } catch (Throwable t) {
            exception = new RpcException("Failed to unregister service to redis registry. registry: " + registryUrl.getAddress() + ", service: " + url + ", cause: " + t.getMessage(), t);
        }
        if (exception != null) {
            if (success) {
                logger.warn(exception.getMessage(), exception);
            } else {
                throw exception;
            }
        }
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) throws RpcException {
        String service = toServicePath(url);
        boolean success = false;
        RpcException exception = null;
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                if (service.endsWith(Constants.ANY_VALUE)) {
                    Set<String> keys = jedis.keys(service);
                    doNotify(jedis, keys);
                }
                success = true;
            } finally {
                jedis.close();
            }
        } catch (Throwable t) { // Try the next server
            exception = new RpcException("Failed to subscribe service from redis registry. registry: " + registryUrl.getAddress() + ", service: " + url + ", cause: " + t.getMessage(), t);
        }
        if (exception != null) {
            if (success) {
                logger.warn(exception.getMessage(), exception);
            } else {
                throw exception;
            }
        }
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {

    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }

    @Override
    public URL getUrl() {
        return registryUrl;
    }

    @Override
    public boolean isAvailable() {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                if (jedis.isConnected()) {
                    return true; // At least one single machine is available.
                }
            } finally {
                jedis.close();
            }
        } catch (Throwable t) {
        }
        return false;
    }

    @Override
    public void destroy() {
        try {
            jedisPool.destroy();
        } catch (Throwable t) {
            logger.warn("Failed to destroy the redis registry client. registry: " + registryUrl.getAddress() + ", cause: " + t.getMessage(), t);
        }
    }

    private class EventSubscriber extends Thread {
        private volatile String service;
        private volatile JedisPool jedisPool;

        public EventSubscriber(JedisPool jedisPool, String service) {
            super.setDaemon(true);
            super.setName("LocustRedisSubscriber");
            this.jedisPool = jedisPool;
            this.service = service;
        }

        @Override
        public void run() {
            try {
                String key = service + Constants.REGISTRY_SEPARATOR + Constants.ANY_VALUE;
                jedisPool.getResource().subscribe(new RegistryEventPubSub(jedisPool), key);

            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }

        public void shutdown() {
            try {
                jedisPool.getResource().disconnect();
            } catch (Throwable t) {
                logger.warn(t.getMessage(), t);
            }
        }
    }

    private class RegistryEventPubSub extends JedisPubSub {
        private Logger logger = LoggerFactory.getLogger(RegistryEventPubSub.class);
        private final JedisPool jedisPool;

        public RegistryEventPubSub(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
        }

        @Override
        public void onMessage(String key, String msg) {
            //TODO doNotify

            if (logger.isInfoEnabled()) {
                logger.info("onMessage: channel[{}], message[{}]", key, msg);
            }
            if (msg.equals(Constants.REGISTER)
                    || msg.equals(Constants.UNREGISTER)) {
                try {
                    Jedis jedis = jedisPool.getResource();
                    try {
                        doNotify(jedis,key);
                    } finally {
                        jedis.close();
                    }
                } catch (Throwable t) {
                    logger.error(t.getMessage(), t);
                }
            }
        }
    }

    private void doNotify(Jedis jedis, Set<String> urls) {
        //TODO doNotify
    }
    private void doNotify(Jedis jedis, String serviceKey) {
        //TODO doNotify
    }
}
