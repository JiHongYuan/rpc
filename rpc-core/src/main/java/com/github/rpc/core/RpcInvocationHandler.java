package com.github.rpc.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author JiHongYuan
 * @date 2022/2/7 16:20
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    private final IDiscover discover;

    private final String version;

    public RpcInvocationHandler(IDiscover serverDiscover, String version) {
        this.discover = serverDiscover;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setVersion(version);
        String serviceName = method.getDeclaringClass().getName();
        if (null != version && !"".equals(version)) {
            serviceName += "-" + version;
        }

        String servicePath = discover.discover(serviceName).get(0);
        if (null == servicePath) {
            log.error("并未找到服务地址,className:{}", serviceName);
            throw new RuntimeException("未找到服务地址");
        }
        String host = servicePath.split(":")[0];
        int port = Integer.parseInt(servicePath.split(":")[1]);
        RpcResponse<?> response = new NettyTransport(host, port).send(request);
        if (response == null) {
            throw new RuntimeException("调用服务失败,servicePath:" + servicePath);
        }

        if (response.getCode() == null || !response.getCode().equals(200)) {
            log.error("调用服务失败,servicePath:{},RpcResponse:{}", servicePath,
                    JSONObject.toJSONString(JSON.toJSONString(response)));
            throw new RuntimeException(response.getMessage());
        } else {
            return response.getData();
        }
    }
}
