package com.github.rpc.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:41
 */
@Slf4j
public class ZkDiscover implements IDiscover {
    private final Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();

    private final CuratorFramework curatorFramework;

    public ZkDiscover(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public List<String> discover(String serviceName) {
        List<String> serviceAddresses;
        if (!serviceAddressMap.containsKey(serviceName)) {
            String path = "/rpc/" + serviceName;
            try {
                serviceAddresses = curatorFramework.getChildren().forPath(path);
                serviceAddressMap.put(serviceName, serviceAddresses);
                registerWatcher(serviceName);
            } catch (KeeperException.NoNodeException e) {
                log.error("未获得该节点,serviceName:{}", serviceName);
                serviceAddresses = null;
            } catch (Exception e) {
                throw new RuntimeException("获取子节点异常：" + e);
            }
        } else {
            serviceAddresses = serviceAddressMap.get(serviceName);
        }
        return serviceAddresses;
    }

    /**
     * 注册监听
     *
     * @param serviceName 服务名称
     */
    private void registerWatcher(String serviceName) {
        String path = "/rpc/" + serviceName;

        CuratorCache cache = CuratorCache.build(curatorFramework, path);
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forAll((type, oldData, data) -> {
                    try {
                        List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
                        serviceAddressMap.put(serviceName, serviceAddresses);
                    } catch (Exception ignored) {
                    }
                })
                .build();
        cache.listenable().addListener(listener);
        cache.start();
    }

}
