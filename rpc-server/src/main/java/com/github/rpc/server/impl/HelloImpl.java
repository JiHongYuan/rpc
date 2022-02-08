package com.github.rpc.server.impl;

import com.github.rpc.api.IHello;
import com.github.rpc.core.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author JiHongYuan
 * @date 2022/2/8 10:07
 */
@Slf4j
@Service
@RpcService(IHello.class)
public class HelloImpl implements IHello {
    @Override
    public String sayHello() {
        return "hello, world!";
    }
}
