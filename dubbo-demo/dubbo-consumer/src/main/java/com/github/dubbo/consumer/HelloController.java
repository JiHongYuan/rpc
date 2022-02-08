package com.github.dubbo.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.dubbo.api.IHello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiHongYuan
 * @date 2022/2/8 15:37
 */
@RestController
public class HelloController {
    @Reference
    private IHello helloService;

    @RequestMapping("/hello")
    public String hello() {
        String hello = helloService.sayHello("world");
        return hello;
    }

}

