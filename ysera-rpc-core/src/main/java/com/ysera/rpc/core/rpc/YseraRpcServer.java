package com.ysera.rpc.core.rpc;

import com.ysera.rpc.core.rpc.processor.RequestProcessor;
import com.ysera.rpc.remote.netty.NettyRemotingServer;
import com.ysera.rpc.remote.netty.NettyServerProperties;
import com.ysera.rpc.remote.protocol.RpcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;

/**
 * @author Administrator
 * @ClassName YseraRpcServer
 * @createTIme 2023年01月19日 20:39:39
 **/
@Component
public class YseraRpcServer {
    private static final Logger logger = LoggerFactory.getLogger(YseraRpcServer.class);

    private NettyRemotingServer nettyRemotingServer;

    @Autowired
    private NettyServerProperties nettyServerProperties;

    @PostConstruct
    private void init() throws RemoteException {
        nettyRemotingServer = new NettyRemotingServer(nettyServerProperties);
        nettyRemotingServer.registerProcessor(RpcType.REQUEST,new RequestProcessor());
        this.nettyRemotingServer.start();

        nettyRemotingServer.registerProcessor(RpcType.REQUEST,new RequestProcessor());
    }

}
