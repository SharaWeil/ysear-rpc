package com.ysera.rpc.core.proxy;

import com.ysera.rpc.core.annotation.RpcClient;
import com.ysera.rpc.exception.RemotingException;
import com.ysera.rpc.remote.Request;
import com.ysera.rpc.remote.Response;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author admin
 * @ClassName RpcProxyAdvice.java
 * @createTime 2023年01月18日 17:58:00
 */
public class RpcProxyAdvice implements InvocationHandler {

    private Invoker invoker;

    public RpcProxyAdvice() {

    }

    public RpcProxyAdvice(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        RpcClient annotation = declaringClass.getAnnotation(RpcClient.class);
        String clazz = method.getDeclaringClass().getName();
        String serviceName = annotation.value();
        int version = annotation.version();
        if (StringUtils.isBlank(serviceName)){
            throw new RemotingException("service name is null");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();

        Request request = new Request(clazz, method.getName(),args, parameterTypes,version);
        Response response = (Response)invoker.invoke(serviceName, request);
        if (response.getThrowable() != null){
            throw response.getThrowable();
        }
        return response.getResult();
    }
}
