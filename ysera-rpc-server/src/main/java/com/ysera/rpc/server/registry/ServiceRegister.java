package com.ysera.rpc.server.registry;

import java.net.InetSocketAddress;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public interface ServiceRegister {
    /**
     *  注册服务
     * @param serviceName 服务名称
     * @param address 服务地址
     */
    void register(String serviceName, InetSocketAddress address);
}
