/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ysera.rpc.remote.netty;

import com.ysera.rpc.exception.RemotingException;
import com.ysera.rpc.exception.RemotingTimeoutException;
import com.ysera.rpc.remote.CallBack;
import com.ysera.rpc.remote.IRpcClient;
import com.ysera.rpc.remote.Response;
import com.ysera.rpc.remote.RpcResponseFuture;
import com.ysera.rpc.remote.codec.NettyDecoder;
import com.ysera.rpc.remote.codec.NettyEncoder;
import com.ysera.rpc.remote.netty.config.NettyClientConfig;
import com.ysera.rpc.remote.netty.handler.NettyRpcClientHandler;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * remoting netty client
 */
public class NettyRemotingClient {

    private final Logger logger = LoggerFactory.getLogger(NettyRemotingClient.class);

    /**
     * client bootstrap
     */
    private final Bootstrap bootstrap = new Bootstrap();

    /**
     * encoder
     */
    private final NettyEncoder encoder = new NettyEncoder();


    /**
     *  decoder
     */
    private final NettyDecoder decoder = new NettyDecoder(Object.class);

    /**
     * client handler
     */
    private final ChannelInboundHandler clientHandler = new NettyRpcClientHandler();


    private final ConcurrentHashMap<InetSocketAddress,Channel> channels = new ConcurrentHashMap<>();


    /**
     * started flag
     */
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    /**
     * worker group
     */
    private final EventLoopGroup workerGroup;

    /**
     * client config
     */
    private final NettyClientConfig clientConfig;

    /**
     * saync semaphore
     */
    private final Semaphore asyncSemaphore = new Semaphore(200, true);


    /**
     * client init
     *
     * @param clientConfig client config
     */
    public NettyRemotingClient(final NettyClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.workerGroup = new NioEventLoopGroup(clientConfig.getWorkerThreads(), new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyClient_%d", this.threadIndex.incrementAndGet()));
            }
        });
        this.start();
    }

    /**
     * start
     */
    private void start() {

        this.bootstrap
                .group(this.workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, clientConfig.isSoKeepalive())
                .option(ChannelOption.TCP_NODELAY, clientConfig.isTcpNoDelay())
                .option(ChannelOption.SO_SNDBUF, clientConfig.getSendBufferSize())
                .option(ChannelOption.SO_RCVBUF, clientConfig.getReceiveBufferSize())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientConfig.getConnectTimeoutMillis())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("encoder", encoder)
                                .addLast("decoder", decoder)
                                .addLast("readTimeoutHandler", new ReadTimeoutHandler(6000))
                                .addLast("handler", clientHandler);
                    }
                });
        isStarted.compareAndSet(false, true);
    }


    public void sendAsync(InetSocketAddress address, RpcProtocol<?> msg, CallBack callBack){
        final Channel channel = getChannel(address);
        if (channel == null) {
            throw new RemotingException(String.format("connect to : %s fail", address));
        }
        long requestId = msg.getMsgHeader().getRequestId();
        int timeoutMillis = 1000;
        RpcResponseFuture responseFuture = new RpcResponseFuture(timeoutMillis,requestId,callBack);

        channel.writeAndFlush(msg).addListener(future -> {
            if (future.isSuccess()){
                responseFuture.setSendOk(true);
                return;
            }else {
                responseFuture.setSendOk(false);
            }
            responseFuture.setThrowable(future.cause());
            responseFuture.putResponse(null);
            logger.error("send command {} to host {} failed", msg, address);
        });
    }





    public Object sendSync(InetSocketAddress address,RpcProtocol<?> msg) throws InterruptedException {
        final Channel channel = getChannel(address);
        if (channel == null) {
            throw new RemotingException(String.format("connect to : %s fail", address));
        }
        int timeoutMillis = 1000;
        RpcResponseFuture responseFuture = new RpcResponseFuture(timeoutMillis,msg.getMsgHeader().getRequestId());

        channel.writeAndFlush(msg).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()){
                responseFuture.setSendOk(true);
                return;
            }else {
                responseFuture.setSendOk(false);
            }
            responseFuture.setThrowable(channelFuture.cause());
            responseFuture.putResponse(null);
            logger.error("send command {} to host {} failed", msg, address);
        });

        /*
         * sync wait for result
         */
        Response result = responseFuture.waitResponse();
        if (result == null) {
            if (responseFuture.isSendOk()) {
                throw new RemotingTimeoutException(address.getHostName(), timeoutMillis, responseFuture.getThrowable());
            } else {
                throw new RemotingException(address.getHostName(), responseFuture.getThrowable());
            }
        }
        return result;
    }


    /**
     * get channel
     */
    public Channel getChannel(InetSocketAddress address) {
        Channel channel = channels.get(address);
        if (channel != null && channel.isActive()) {
            return channel;
        }
        return createChannel(address, true);
    }

    /**
     *  create channel
     * @param address
     * @param isSync
     * @return
     */
    public Channel createChannel(InetSocketAddress address,boolean isSync){
        ChannelFuture future;
        try {
            synchronized (bootstrap) {
                future = bootstrap.connect(address);
            }
            if (isSync) {
                future.sync();
            }
            if (future.isSuccess()) {
                Channel channel = future.channel();
                channels.put(address, channel);
                return channel;
            }
        } catch (Exception ex) {
            logger.warn(String.format("connect to %s error", address), ex);
        }
        return null;
    }

}
