package com.ysera.rpc.remote;

import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;

import java.net.InetSocketAddress;

/**
 * @author Administrator
 * @ClassName IRpcClient
 * @createTIme 2023年01月19日 11:14:14
 */
public interface IRpcClient {

    /**
     *  异步执行任务
     * @param protocol
     * @param address
     * @param callBack
     */
    void sendAsync(RpcProtocol<?> protocol, InetSocketAddress address, CallBack callBack);


    /**
     *  同步执行任务
     * @param protocol
     * @param address
     * @return Response
     */
    Response sendSync(RpcProtocol<?> protocol, InetSocketAddress address);
}
