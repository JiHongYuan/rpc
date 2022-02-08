package com.github.rpc.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author JiHongYuan
 * @date 2022/2/8 9:05
 */
@Data
@ConfigurationProperties("rpc.zookeeper")
public class ZookeeperProperties {
    private String ip;
    private String port;
}
