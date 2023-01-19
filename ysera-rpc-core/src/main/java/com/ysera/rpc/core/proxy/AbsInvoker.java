package com.ysera.rpc.core.proxy;

import com.ysera.rpc.core.balance.LoadBalance;
import com.ysera.rpc.core.balance.RandomBalance;
import com.ysera.rpc.core.registry.RegistryClient;
import com.ysera.rpc.exception.RemotingException;
import com.ysera.rpc.remote.IRpcClient;
import com.ysera.rpc.remote.Request;
import com.ysera.rpc.remote.protocol.RpcConstants;
import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import com.ysera.rpc.remote.protocol.RpcType;
import com.ysera.rpc.remote.serializer.RpcSerializerType;
import com.ysera.rpc.remote.serializer.RpcCompressType;
import com.ysera.rpc.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.List;

/*
 * @author Administrator
 * @ClassName AbsInvoker
 * @createTIme 2023年01月19日 10:43:43
 **/
@Component
public class AbsInvoker implements Invoker{

    @Autowired
    private RegistryClient registryClient;

    private LoadBalance loadBalance;

    @Autowired
    private IRpcClient client;

    @PostConstruct
    public void init(){
        loadBalance = new RandomBalance();
    }

    @Override
    public Object invoke(String serviceName, Request request) {
        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagic(RpcConstants.MAGIC_NUMBER);
        rpcHeader.setVersion((byte) request.getVersion());
        rpcHeader.setRpcType(RpcType.REQUEST.getType());
        rpcHeader.setCompress(RpcCompressType.NONE.getType());
        rpcHeader.setSerialization(RpcSerializerType.KRYO.getType());
        long requestId = RandomUtil.randomLong();
        rpcHeader.setRequestId(requestId);
        RpcProtocol<Request> protocol = new RpcProtocol<>();
        protocol.setBody(request);
        protocol.setMsgHeader(rpcHeader);
        return client.sendSync(protocol,getRemoteAddress(serviceName));
    }

    private InetSocketAddress getRemoteAddress(String serviceName){
        List<InetSocketAddress> children = registryClient.children(serviceName);
        if (children.isEmpty()){
            throw new RemotingException("No services available");
        }
        return loadBalance.selectOne(children);
    }
}
