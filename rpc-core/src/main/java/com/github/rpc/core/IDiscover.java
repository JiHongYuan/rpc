package com.github.rpc.core;

import java.util.List;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:41
 */
public interface IDiscover {

    /**
     * 基于服务名称获得一个远程地址
     *
     * @param serviceName 服务名称
     * @return 远程地址
     */
    List<String> discover(String serviceName);
}
