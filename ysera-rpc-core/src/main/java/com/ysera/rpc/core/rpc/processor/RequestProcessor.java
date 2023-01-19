package com.ysera.rpc.core.rpc.processor;

import com.ysera.rpc.core.annotation.RpcService;
import com.ysera.rpc.remote.Request;
import com.ysera.rpc.remote.Response;
import com.ysera.rpc.remote.SpringBeanUtil;
import com.ysera.rpc.remote.process.NettyRequestProcessor;
import io.netty.channel.Channel;

import java.lang.reflect.Method;

/*
 * @author Administrator
 * @ClassName RequestProcessor
 * @createTIme 2023年01月19日 20:43:43
 **/
public class RequestProcessor implements NettyRequestProcessor {
    @Override
    public void process(Channel channel, Request request) {
        try {
            Response response = new Response();
            Object[] arguments = request.getArguments();
            Method method = request.getMethod();
            String clazzName = request.getClazzName();
            Class<?>[] paramType = request.getParamType();
            int version = request.getVersion();
            Object bean = SpringBeanUtil.getBean(clazzName);
            Method invokeMethod = bean.getClass().getMethod(method.getName(), paramType);
            invokeMethod.setAccessible(true);
            Object invoke = invokeMethod.invoke(bean, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
