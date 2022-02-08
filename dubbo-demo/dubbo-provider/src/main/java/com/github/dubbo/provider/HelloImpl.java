package com.github.dubbo.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.dubbo.api.IHello;
import org.springframework.stereotype.Component;

/**
 * @author JiHongYuan
 * @date 2022/2/8 15:38
 */
@Component
@Service
public class HelloImpl implements IHello {
    @Override
    public String sayHello(String text) {
        return "Hello " + text;
    }
}
