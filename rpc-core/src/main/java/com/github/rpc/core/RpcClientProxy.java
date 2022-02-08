package com.github.rpc.core;

import java.lang.reflect.Proxy;

/**
 * @author JiHongYuan
 * @date 2022/2/7 16:19
 */
public class RpcClientProxy {
    private IDiscover discover;

    public RpcClientProxy(IDiscover discover) {
        this.discover = discover;
    }


    /**
     * 基于服务接口和版本创建代理
     *
     * @param interfaceCls 服务接口
     * @param version      版本
     * @param <T>          泛型
     *
     * @return 实现该节点的代理对象
     */
    public <T> T clientProxy(Class<T> interfaceCls, String version) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[] { interfaceCls },
                new RpcInvocationHandler(discover, version));
    }

    public <T> T clientProxy(Class<T> interfaceCls) {
        return this.clientProxy(interfaceCls, null);
    }
}
