package com.ysera.rpc.remote.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author admin
 * @ClassName NettyServerProperties.java
 * @createTime 2023年01月18日 17:03:00
 */
@Configuration
@ConfigurationProperties(prefix = "rpc.netty")
public class NettyServerProperties {

    private int port = 5050;

    private String address;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
