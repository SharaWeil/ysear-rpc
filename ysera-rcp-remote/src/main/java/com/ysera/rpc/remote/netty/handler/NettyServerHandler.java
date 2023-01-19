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
import com.ysera.rpc.util.Pair;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
//        try {
//            log.info("client receive msg:{}", msg);
//            if (msg instanceof RpcProtocol) {
//                RpcProtocol rpcProtocol = (RpcProtocol) msg;
//                RpcHeader rpcHeader = rpcProtocol.getMsgHeader();
//                RpcType rpcType = RpcType.getRpcType(rpcHeader.getRpcType());
//                Object body = rpcProtocol.getBody();
//                if (rpcType == RpcType.HEARTBEAT) {
//                    log.info("heart:[{}]", body);
//                    return;
//                }
//                if (rpcType == RpcType.REQUEST) {
//                    Request request = (Request) body;
//                    threadPoolExecutor.execute(() -> {
//                        try {
//                            Response response = new Response();
//                            Object[] arguments = request.getArguments();
//                            Method method = request.getMethod();
//                            String clazzName = request.getClazzName();
//                            Class<?>[] paramType = request.getParamType();
//                            int version = request.getVersion();
////                            Class.forName(clazzName).getAnnotation(Rpc)
////                            SpringBeanUtil.getBean()
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
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
