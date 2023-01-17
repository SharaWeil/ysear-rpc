package com.ysera.rpc.server.registry.zk;

import com.ysera.rpc.exception.RegistryException;
import com.ysera.rpc.server.registry.ServiceRegister;
import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.awt.datatransfer.ClipboardOwner;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/*
 * @Author Administrator
 * @Date 2023/1/17
 *                        /root
 *                         /
 *                    /ysera-rpc
 *                  /         \
 *        /service-name1      /service-name2
 *           /                     \
 *     [[ip:port],[ip:port]]    [[ip:port],[ip:port]]
 *
 **/
public class ZkServiceRegisterImpl implements ServiceRegister {
    private static final Logger log = LoggerFactory.getLogger(ZkServiceRegisterImpl.class);

    private final CuratorFramework client;
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final long CONNECTION_TIME_OUT = 10;
    private static final long SESSION_TIME_OUT = 30;
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    @Override
    public void register(String serviceName, InetSocketAddress address) {
       ZkUtil.put(client,serviceName,address.toString(),true);
    }

    public ZkServiceRegisterImpl(Properties properties) {
        CuratorFrameworkFactory.Builder build = CuratorFrameworkFactory.builder()
                .connectString(DEFAULT_ZOOKEEPER_ADDRESS)
                .namespace("/ysera-rpc")
                .connectionTimeoutMs((int) Duration.ofSeconds(CONNECTION_TIME_OUT).toMillis())
                .sessionTimeoutMs((int) Duration.ofSeconds(SESSION_TIME_OUT).toMillis())
                .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES));
        client = build.build();
    }

    @PostConstruct
    public void start() {
        client.start();
        try {
            if (!client.blockUntilConnected(30, TimeUnit.SECONDS)) {
                client.close();
                throw new RegistryException("zookeeper connect timeout: " + DEFAULT_ZOOKEEPER_ADDRESS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
