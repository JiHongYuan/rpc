package com.github.rpc.spring;

import com.github.rpc.core.RpcClientProxy;
import com.github.rpc.core.RpcService;
import com.github.rpc.core.ZkServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * @author JiHongYuan
 * @date 2022/2/8 9:26
 */
@Component
@ConditionalOnClass(ZkServer.class)
public class RpcBeanPostProcessor implements BeanPostProcessor {
    @Value("${rpc.type}")
    private String type;

    private final ZkServer zkServer;
    private final RpcClientProxy clientProxy;

    public RpcBeanPostProcessor(ZkServer zkServer) {
        this.zkServer = zkServer;
        clientProxy = new RpcClientProxy(zkServer.getDiscover());
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getAnnotation(RpcService.class) != null) {
            zkServer.bind(bean);
            if (type.equals("client")) {
                return clientProxy.clientProxy(bean.getClass().getInterfaces()[0]);
            }
        }
        return bean;
    }

}