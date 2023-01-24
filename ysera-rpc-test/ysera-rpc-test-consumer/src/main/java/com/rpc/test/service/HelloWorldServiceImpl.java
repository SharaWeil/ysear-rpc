package com.rpc.test.service;


import com.ysera.rpc.core.annotation.RpcService;

/**
 * @author admin
 * @ClassName HelloWorld.java
 * @createTime 2023年01月24日 10:42:00
 */
@RpcService(serviceName = "rpc-helloWorld",version = 1)
public class HelloWorldServiceImpl implements Service {

    private String name;
    @Override
    public String sayHello(String name) {
        return "hello, "+name+"!,my name is"+ name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
