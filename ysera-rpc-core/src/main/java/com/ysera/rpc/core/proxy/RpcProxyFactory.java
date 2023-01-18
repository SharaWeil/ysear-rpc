package com.ysera.rpc.core.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author admin
 * @ClassName ProxyFactory.java
 * @createTime 2023年01月18日 17:52:00
 */
public class RpcProxyFactory {

    public static <T> T newProxy(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        return ProxyFactory.getProxy(tClass, new RpcProxyAdvice());
    }

}
