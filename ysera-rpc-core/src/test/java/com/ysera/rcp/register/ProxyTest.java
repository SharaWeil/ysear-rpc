package com.ysera.rcp.register;

import com.ysera.rpc.core.HelloService;
import com.ysera.rpc.core.proxy.RpcProxyFactory;
import org.junit.Test;

/**
 * @author admin
 * @ClassName ProxyTest.java
 * @createTime 2023年01月18日 18:14:00
 */
public class ProxyTest {
    @Test
    public void testProxy() throws InstantiationException, IllegalAccessException {
        HelloService helloService = RpcProxyFactory.newProxy2(HelloService.class);
        System.out.println(helloService.sayHello("tom"));
    }
}
