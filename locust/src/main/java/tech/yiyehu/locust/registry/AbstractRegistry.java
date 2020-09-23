package tech.yiyehu.locust.registry;

import tech.yiyehu.locust.common.Constants;
import tech.yiyehu.locust.common.Node;
import tech.yiyehu.locust.common.URL;
import tech.yiyehu.locust.rpc.exception.RpcException;

public abstract class AbstractRegistry implements Registry, Node {
    protected URL registryUrl;

    public AbstractRegistry() {
    }

    public AbstractRegistry(URL url) throws RpcException {
        checkURL(url);
        this.registryUrl = url;
    }

    private void checkURL(URL url) throws RpcException {
        if (!Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
            throw new RpcException("this is not registry protocol", null);
        }
    }
}
