package com.github.rpc;

import com.github.rpc.api.IHello;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author JiHongYuan
 * @date 2022/2/8 9:54
 */
@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ClientApplication.class, args);

        IHello hello = run.getBean(IHello.class);
        System.out.println(hello.sayHello());
    }
}
