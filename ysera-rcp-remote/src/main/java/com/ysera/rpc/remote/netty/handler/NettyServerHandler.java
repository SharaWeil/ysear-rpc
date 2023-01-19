package com.ysera.rpc.remote.netty.handler;

import com.ysera.rpc.remote.Request;
import com.ysera.rpc.remote.Response;
import com.ysera.rpc.remote.RpcResponseFuture;
import com.ysera.rpc.remote.SpringBeanUtil;
import com.ysera.rpc.remote.process.NettyRequestProcessor;
import com.ysera.rpc.remote.protocol.RpcConstants;
import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import com.ysera.rpc.remote.protocol.RpcType;
import com.ysera.rpc.remote.util.ChannelUtils;
import com.ysera.rpc.util.Pair;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jdk.nashorn.internal.objects.NativeUint8Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author admin
 * @ClassName NettyServerHandler.java
 * @createTime 2023年01月18日 17:21:00
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);


    private final ExecutorService defaultExecutor = Executors.newFixedThreadPool(RpcConstants.CPUS);

    private ConcurrentHashMap<RpcType, Pair<NettyRequestProcessor,ExecutorService>> processors = new ConcurrentHashMap<>();

    public NettyServerHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
         log.info("client receive msg:{}", msg);

        if (msg instanceof RpcProtocol) {
            RpcProtocol<?> rpcProtocol = (RpcProtocol<?>) msg;
            RpcHeader rpcHeader = rpcProtocol.getMsgHeader();
            RpcType rpcType = RpcType.getRpcType(rpcHeader.getRpcType());
            Object body = rpcProtocol.getBody();
            if (rpcType == RpcType.HEARTBEAT) {
                log.info("heart:[{}]", body);
                return;
            }
            final Pair<NettyRequestProcessor, ExecutorService> pair = processors.get(rpcType);
            Channel channel = ctx.channel();
            if (null != pair){
                Runnable runnable = () -> {
                    try {
                        pair.getLeft().process(channel, (Request) body);
                    } catch (Exception ex) {
                        log.error("process msg {} error", msg, ex);
                    }
                };
                try {
                    pair.getRight().submit(runnable);
                } catch (RejectedExecutionException e) {
                    log.warn("thread pool is full, discard msg {} from {}", msg, ChannelUtils.getRemoteAddress(channel));
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * register processor
     *
     * @param rpcType rpc type
     * @param processor processor
     * @param executor thread executor
     */
    public void registerProcessor(final RpcType rpcType, final NettyRequestProcessor processor, final ExecutorService executor) {
        ExecutorService executorRef = executor;
        if (executorRef == null) {
            executorRef = defaultExecutor;
        }
        this.processors.putIfAbsent(rpcType, new Pair<>(processor, executorRef));
    }
}
