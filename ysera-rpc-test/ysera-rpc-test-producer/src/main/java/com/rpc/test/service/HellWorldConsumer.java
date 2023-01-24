package com.rpc.test.service;

import com.ysera.rpc.core.annotation.RpcClient;

/**
 * @author admin
 * @ClassName HellWorldConsumer.java
 * @createTime 2023年01月24日 22:09:00
 */
@RpcClient(value = "rpc-helloWorld",version = 1)
public interface HellWorldConsumer {

    public String sayHello(String name);
}
