package com.ysera.rpc.remote.netty.handler;

import com.ysera.rpc.remote.Response;
import com.ysera.rpc.remote.RpcResponseFuture;
import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import com.ysera.rpc.remote.protocol.RpcType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Administrator
 * @ClassName NettyRpcClientHandler
 * @createTIme 2023年01月19日 16:12:12
 **/
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(NettyRpcClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.info("client receive msg:{}",msg);
            if (msg instanceof RpcProtocol){
                RpcProtocol rpcProtocol = (RpcProtocol) msg;
                RpcHeader rpcHeader = rpcProtocol.getMsgHeader();
                RpcType rpcType = RpcType.getRpcType(rpcHeader.getRpcType());
                if (rpcType == RpcType.HEARTBEAT){
                    log.info("heart:[{}]",rpcProtocol.getBody());
                    return;
                }
                if (rpcType == RpcType.RESPONSE){
                    RpcResponseFuture future = RpcResponseFuture.getResponseFuture(rpcHeader.getRequestId());
                    Response response = (Response) rpcProtocol.getBody();
                    future.setResponse(response);
                    if (future.getCallBack() != null){
                        future.executeInvokeCallback();
                    }
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
