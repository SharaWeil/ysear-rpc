package com.ysera.rpc.core;

import com.ysera.rpc.core.annotation.RpcConsumer;

/**
 * @author admin
 * @ClassName HelloService.java
 * @createTime 2023年01月18日 17:51:00
 */
@RpcConsumer(serviceName = "test",clazz = "com.ysera.rpc.test.HelloService",version = 1)
public interface HelloService {

    /**
     *  测试服务
     * @param name 姓名
     */
    String sayHello(String name);
}
