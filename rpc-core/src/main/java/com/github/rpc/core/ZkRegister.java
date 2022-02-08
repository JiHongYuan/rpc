package com.github.rpc.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:22
 */
@Slf4j
public class ZkRegister implements IRegister {

    public static final String ZK_REGISTER_PATH = "/rpc";

    private final CuratorFramework curatorFramework;

    public ZkRegister(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public void register(String serviceName, String serviceAddress) throws Exception {
        //需要注册的服务根节点
        String servicePath = ZK_REGISTER_PATH + "/" + serviceName;
        //注册服务，创建临时节点
        String serviceAddr = servicePath + "/" + serviceAddress;
        String nodePath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(serviceAddr, "".getBytes());
        log.debug("节点创建成功，节点为:{}", nodePath);
    }

}
