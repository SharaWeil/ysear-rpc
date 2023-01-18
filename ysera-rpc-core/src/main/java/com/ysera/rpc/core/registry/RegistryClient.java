package com.ysera.rpc.core.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * @author admin
 * @ClassName RegistryClient.java
 * @createTime 2023年01月18日 16:29:00
 */
@Component
public class RegistryClient {

    private static final Logger log = LoggerFactory.getLogger(RegistryClient.class);

    private static final String PATH_SEPARATOR = File.separator;
    private static final String EMPTY = "";

    private Registry registry;


    public RegistryClient(Registry registry) {
        this.registry =  registry;
    }

    /**
     *  注册服务
     * @param serviceName 服务名称
     * @param address 服务地址
     */
    public void register(String serviceName, InetSocketAddress address) {
        serviceName = checkPath(serviceName);
        String path = serviceName + PATH_SEPARATOR + address;
        registry.put(path, EMPTY, true);
        log.info("register zk success! path:[{}]",path);
    }

    private String checkPath(String path) {
        if (!path.startsWith(PATH_SEPARATOR)) {
            path = PATH_SEPARATOR + path;
        }
        return path;
    }

}
