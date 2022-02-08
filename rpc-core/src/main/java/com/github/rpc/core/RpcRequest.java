package com.github.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JiHongYuan
 * @date 2022/2/7 11:16
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 5661720043123218215L;

    /**
     * 请求接口名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数数组
     */
    private Object[] params;

    /**
     * 版本号
     */
    private String version;
}