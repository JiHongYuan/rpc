package com.github.rpc.spring;

import com.github.rpc.core.ZkServer;
import com.github.rpc.spring.properties.ServerProperties;
import com.github.rpc.spring.properties.ZookeeperProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiHongYuan
 * @date 2022/2/8 9:03
 */
@Data
@Configuration
@EnableConfigurationProperties({ServerProperties.class, ZookeeperProperties.class, RpcAutoConfigure.class})
@ConfigurationProperties("rpc")
public class RpcAutoConfigure {
    private String type;
    private final ZookeeperProperties zookeeperProperties;
    private final ServerProperties serverProperties;

    public RpcAutoConfigure(ZookeeperProperties zookeeperProperties, ServerProperties serverProperties) {
        this.zookeeperProperties = zookeeperProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    public ZkServer zkServer() {
        ZkServer zkServer = new ZkServer(zookeeperProperties.getIp() + ":" + zookeeperProperties.getPort(), type);
        if ("server".equals(type)) {
            zkServer.setServiceIp(serverProperties.getIp());
            zkServer.setServicePort(serverProperties.getPort());
            zkServer.start();
        }
        return zkServer;
    }
}
