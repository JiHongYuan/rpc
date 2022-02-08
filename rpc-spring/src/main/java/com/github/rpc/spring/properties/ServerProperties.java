package com.github.rpc.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author JiHongYuan
 * @date 2022/2/8 9:05
 */
@Data
@ConfigurationProperties("rpc.server")
public class ServerProperties {
    private String ip;
    private int port;
}
