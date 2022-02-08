package com.github.rpc.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 提供服务的注解
 *
 * @author JiHongYuan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * 对外发布的服务接口地址
     */
    Class<?> value();

    /**
     * 版本
     */
    String version() default "";

}