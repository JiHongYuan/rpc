package com.github.rpc.core.impl;

import com.github.rpc.core.IHello;
import com.github.rpc.core.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JiHongYuan
 * @date 2022/2/7 15:52
 */
@RpcService(IHello.class)
@Slf4j
public class IHelloImpl implements IHello {
    @Override
    public String sayHello() {
        log.info("say, hello!");
        return "say, hello!";
    }
}
