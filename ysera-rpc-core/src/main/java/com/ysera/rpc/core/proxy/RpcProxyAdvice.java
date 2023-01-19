package com.ysera.rpc.core.proxy;

import com.ysera.rpc.core.annotation.RpcService;
import com.ysera.rpc.exception.RemotingException;
import com.ysera.rpc.remote.Request;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author admin
 * @ClassName RpcProxyAdvice.java
 * @createTime 2023年01月18日 17:58:00
 */
public class RpcProxyAdvice implements MethodInterceptor {

    private Invoker invoker;

    public RpcProxyAdvice() {

    }

    public RpcProxyAdvice(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        Method method = invocation.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        RpcService annotation = declaringClass.getAnnotation(RpcService.class);
        String clazz = annotation.clazz();
        String serviceName = annotation.serviceName();
        int version = annotation.version();
        if (StringUtils.isBlank(serviceName)){
            throw new RemotingException("service name is null");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();

        Request request = new Request(clazz, method,arguments, parameterTypes,version);

        return invoker.invoke(serviceName,request);
    }
}
