package com.ysera.rpc.core.registry;

import com.ysera.rpc.exception.RegistryException;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public interface Registry {

    /**
     *  添加节点
     * @param key
     * @param value
     * @param deleteOnDisconnect
     */
    void put(String key, String value, boolean deleteOnDisconnect);

    /**
     *  获取子节点
     * @param key
     * @return
     */
    List<String> children(String key);

    /**
     *  判断节点是否存在
     * @param serviceName
     * @return
     */
    Boolean exist(String serviceName);
}
