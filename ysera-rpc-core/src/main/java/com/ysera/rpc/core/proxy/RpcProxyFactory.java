package com.ysera.rpc.core.proxy;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @ClassName ProxyFactory.java
 * @createTime 2023年01月18日 17:52:00
 */
@Component
public class RpcProxyFactory {

    @Autowired
    Invoker invoker;

    public <T> T newProxy(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        return ProxyFactory.getProxy(tClass, new RpcProxyAdvice(invoker));
    }

    public static <T> T newProxy2(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        return ProxyFactory.getProxy(tClass, new RpcProxyAdvice());
    }

}
