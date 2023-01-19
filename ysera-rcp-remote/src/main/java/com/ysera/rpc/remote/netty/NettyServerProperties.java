package com.ysera.rpc.remote.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author admin
 * @ClassName NettyServerProperties.java
 * @createTime 2023年01月18日 17:03:00
 */
@Configuration
@ConfigurationProperties(prefix = "ysera.rpc.netty")
public class NettyServerProperties {

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
