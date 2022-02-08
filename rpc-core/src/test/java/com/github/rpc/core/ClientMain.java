package com.github.rpc.core;

import com.github.rpc.core.impl.IHelloImpl;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:33
 */
public class ClientMain {
    public static void main(String[] args) {
        ZkServer server = new ZkServer("127.0.0.1:2181","client");
        RpcClientProxy clientProxy = new RpcClientProxy(server.getDiscover());
        IHello hello = clientProxy.clientProxy(IHello.class);
        System.out.println(hello.sayHello());
    }
}
