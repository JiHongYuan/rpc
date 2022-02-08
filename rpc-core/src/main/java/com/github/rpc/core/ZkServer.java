package com.github.rpc.core;

import com.github.rpc.core.load.RandomLoadBalanceDiscover;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JiHongYuan
 * @date 2022/2/7 10:32
 */
@Slf4j
public class ZkServer {
    @Getter
    private final IRegister register;
    @Getter
    private final IDiscover discover;
    @Getter
    private final CuratorFramework curatorFramework;

    /**
     * 服务名称和服务对象的关系
     */
    private final Map<String, Object> handlerMap = new HashMap<>(16);

    @Setter
    private String serviceIp;
    @Setter
    private int servicePort;

    private final String type;

    public ZkServer(String connectionAddress, String type) {
        this.type = type;

        //初始化curator
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectionAddress)
                .sessionTimeoutMs(15000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        register = new ZkRegister(curatorFramework);

        // 负载均衡
        discover = new RandomLoadBalanceDiscover(new ZkDiscover(curatorFramework));

        curatorFramework.start();
    }

    public ZkServer(String connectionAddress, String serviceIp, int servicePort, String type) {
        this(connectionAddress, type);
        this.serviceIp = serviceIp;
        this.servicePort = servicePort;
    }

    public void bind(Object... services) {
        for (Object service : services) {
            RpcService anno = service.getClass().getAnnotation(RpcService.class);
            if (null == anno) {
                //注解为空的情况，version就是空，serviceName就是
                throw new RuntimeException("服务并没有注解，请检查。" + service.getClass().getName());
            }
            String serviceName = anno.value().getName();
            String version = anno.version();
            if (!"".equals(version)) {
                serviceName += "-" + version;
            }
            handlerMap.put(serviceName, service);
            if (type.equals("server")) {
                register(serviceName);
            }
        }
    }

    public void start() {
        try {
            //使用netty开启一个服务
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
            bootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            //数据分包，组包，粘包
                            p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            p.addLast(new LengthFieldPrepender(4));
                            p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            p.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            p.addLast(new ProcessRequestHandler(handlerMap));
                        }
                    });
            bootstrap.bind(serviceIp, servicePort).sync();
            log.info("成功启动服务,host:{},port:{}", serviceIp, servicePort);
        } catch (InterruptedException ignored) {
        }
    }

    private void register(String serviceName) {
        try {
            register.register(serviceName, serviceIp + ":" + servicePort);
        } catch (Exception e) {
            log.error("服务注册失败,e:{}", e.getMessage());
            throw new RuntimeException("服务注册失败");
        }
        log.info("成功注册服务，服务名称：{},服务地址：{}", serviceName, serviceIp + ":" + servicePort);
    }

}