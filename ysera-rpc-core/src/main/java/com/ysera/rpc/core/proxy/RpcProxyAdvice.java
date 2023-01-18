package com.ysera.rpc.core.proxy;

import com.ysera.rpc.remote.netty.NettyClient;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author admin
 * @ClassName RpcProxyAdvice.java
 * @createTime 2023年01月18日 17:58:00
 */
public class RpcProxyAdvice implements MethodInterceptor {

    private NettyClient client;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        Class<?> declaringClass = invocation.getMethod().getDeclaringClass();
        Annotation[] annotations = declaringClass.getAnnotations();

        return "代理发送的信息";
    }
}
