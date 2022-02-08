package com.github.rpc.core;

import com.github.rpc.core.impl.IHelloImpl;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:33
 */
public class Server1Main {
    public static void main(String[] args) {
        ZkServer server = new ZkServer("127.0.0.1:2181","127.0.0.1", 8001, "server");

        IHello hello = new IHelloImpl();
        server.bind(hello);
        server.start();
    }
}
