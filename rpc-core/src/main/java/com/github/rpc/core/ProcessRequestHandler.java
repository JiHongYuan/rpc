package com.github.rpc.core;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JiHongYuan
 * @date 2022/2/7 11:13
 */
@Slf4j
public class ProcessRequestHandler extends SimpleChannelInboundHandler<String> {

    private Map<String, Object> handlerMap;

    public ProcessRequestHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        log.debug("收到request:{}", s);
        Object result = this.invoke(JSON.parseObject(s, RpcRequest.class));
        ChannelFuture future = channelHandlerContext.writeAndFlush(JSON.toJSONString(result));
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }

    /**
     * 服务调用返回处理结果
     *
     * @param request 服务请求
     * @return 处理结果
     */
    private Object invoke(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获得服务名称
        String serviceName = request.getClassName();
        // 获得版本号
        String version = request.getVersion();
        // 获得方法名
        String methodName = request.getMethodName();
        // 获得参数数组
        Object[] params = request.getParams();

        Class<?>[] argTypes;
        if (params != null && params.length > 0) {
            argTypes = Arrays.stream(params).map(Object::getClass).toArray(Class<?>[]::new);
        } else {
            argTypes = new Class[]{};
        }

        if (version != null && !"".equals(version)) {
            serviceName = serviceName + "-" + version;
        }

        Object service = handlerMap.get(serviceName);
        if (null == service) {
            return RpcResponse.fail(404, "未找到服务");
        }

        Method method;
        try {
            method = service.getClass().getMethod(methodName, argTypes);
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(404, "未找到服务方法");
        }

        return RpcResponse.success(method.invoke(service, params));
    }

}
