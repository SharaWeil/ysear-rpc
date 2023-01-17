package com.ysera.rpc.server.registry.zk;

import com.ysera.rpc.server.registry.ServiceRegister;

import java.net.InetSocketAddress;

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
    @Override
    public void register(String serviceName, InetSocketAddress address) {

    }
}
