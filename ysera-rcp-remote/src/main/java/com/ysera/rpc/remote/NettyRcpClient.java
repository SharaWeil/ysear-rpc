package com.ysera.rpc.remote;

import com.ysera.rpc.remote.netty.NettyRemotingClient;
import com.ysera.rpc.remote.netty.config.NettyClientConfig;
import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author Administrator
 * @ClassName NettyRcpClient
 * @createTIme 2023年01月19日 11:16:16
 **/
@Component
public class NettyRcpClient implements IRpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyRcpClient.class);

    private NettyRemotingClient client;

    private ThreadPoolExecutor threadPoolExecutor;

    public NettyRcpClient() {
    }

    @PostConstruct
    public void init(){
        logger.info("Worker rpc client starting");
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        client = new NettyRemotingClient(nettyClientConfig);
        threadPoolExecutor = new ThreadPoolExecutor(50, 100, 30,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("SendRequestHandler_%d", this.threadIndex.incrementAndGet()));
            }
        });
    }

    @Override
    public void sendAsync(RpcProtocol<?> protocol, InetSocketAddress address, CallBack callBack) {
        try {
            threadPoolExecutor.execute(()->{
                CountDownLatch downLatch = new CountDownLatch(1);
                client.sendAsync(address, protocol, response -> {
                    downLatch.countDown();
                    callBack.completed(response);
                });
                try {
                    downLatch.await();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Response sendSync(RpcProtocol<?> protocol, InetSocketAddress address) {
        try {
            return (Response)client.sendSync(address, protocol);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        return null;
    }
}
