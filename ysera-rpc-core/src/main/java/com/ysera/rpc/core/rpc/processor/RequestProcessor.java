package com.ysera.rpc.core.rpc.processor;

import com.ysera.rpc.core.annotation.RpcService;
import com.ysera.rpc.remote.Request;
import com.ysera.rpc.remote.Response;
import com.ysera.rpc.remote.util.SpringBeanUtil;
import com.ysera.rpc.remote.process.NettyRequestProcessor;
import com.ysera.rpc.remote.protocol.RpcConstants;
import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import com.ysera.rpc.remote.protocol.RpcType;
import com.ysera.rpc.remote.serializer.RpcCompressType;
import com.ysera.rpc.remote.serializer.RpcSerializerType;
import io.netty.channel.Channel;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author Administrator
 * @ClassName RequestProcessor
 * @createTIme 2023年01月19日 20:43:43
 **/
public class RequestProcessor implements NettyRequestProcessor {

    @Override
    public void process(Channel channel, RpcProtocol<?> rpcProtocol) {
        Response response = new Response();
        Request request = (Request) rpcProtocol.getBody();
        RpcHeader header = rpcProtocol.getMsgHeader();
        response.setRequestId(header.getRequestId());
        int version = 0;
        Object result = null;
        Throwable throwable = null;
        try {
            Object[] arguments = request.getArguments();
            String method = request.getMethod();
            String clazzName = request.getClazzName();
            Class<?>[] paramType = request.getParamType();
            Class<?> tClass = Class.forName(clazzName);
            Object bean = SpringBeanUtil.getBean(tClass);
            version = request.getVersion();
            checkVersion(tClass,version);
            Method invokeMethod = bean.getClass().getMethod(method, paramType);
            invokeMethod.setAccessible(true);
            result = invokeMethod.invoke(bean, arguments);
        } catch (Exception e) {
            e.printStackTrace();
            throwable = e;
        }
        response.setVersion(version);
        response.setResult(result);
        response.setThrowable(throwable);
        channel.writeAndFlush(convertRpcProtocol(response));
    }

    private void checkVersion(Class<?> tClass, int version) {
        RpcService annotation = tClass.getAnnotation(RpcService.class);
        Assert.isTrue(annotation.version() == version,
                MessageFormat.format( "Service version is inconsistent,request version:{0},service version:{1}",version,annotation.version()));
    }

    private RpcProtocol<Response> convertRpcProtocol(Response response) {
        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagic(RpcConstants.MAGIC_NUMBER);
        rpcHeader.setVersion((byte) response.getVersion());
        rpcHeader.setRpcType(RpcType.RESPONSE.getType());
        rpcHeader.setCompress(RpcCompressType.NONE.getType());
        rpcHeader.setSerialization(RpcSerializerType.KRYO.getType());
        rpcHeader.setRequestId(response.getRequestId());
        RpcProtocol<Response> protocol = new RpcProtocol<>();
        protocol.setBody(response);
        protocol.setMsgHeader(rpcHeader);
        return protocol;
    }
}
