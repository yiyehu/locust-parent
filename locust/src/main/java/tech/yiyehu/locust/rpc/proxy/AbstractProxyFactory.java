/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.yiyehu.locust.rpc.proxy;

import tech.yiyehu.locust.rpc.Invoker;
import tech.yiyehu.locust.rpc.exception.RpcException;

import java.util.HashSet;
import java.util.Set;

/**
 * AbstractProxyFactory
 */
public abstract class AbstractProxyFactory {

    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        Set<Class<?>> interfaces = new HashSet<>();

        interfaces.add(invoker.getInterface());

        return getProxy(invoker, interfaces.toArray(new Class<?>[0]));
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);

}