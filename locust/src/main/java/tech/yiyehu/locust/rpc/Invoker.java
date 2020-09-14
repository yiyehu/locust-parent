package tech.yiyehu.locust.rpc;

import tech.yiyehu.locust.common.Node;
import tech.yiyehu.locust.rpc.exception.RpcException;

public interface Invoker<T> extends Node {
    /**
     * get service interface.
     *
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     *
     * @param invocation
     * @return result
     * @throws RpcException
     */
    Result invoke(Invocation invocation) throws RpcException;
}
