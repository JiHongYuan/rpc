package com.github.rpc.core;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:22
 */
public interface IRegister {

    /**
     * 基于服务名和服务地址注册一个服务
     *
     * @param serviceName    服务名称
     * @param serviceAddress 服务地址
     * @throws Exception 节点创建失败
     */
    void register(String serviceName, String serviceAddress) throws Exception;
}
