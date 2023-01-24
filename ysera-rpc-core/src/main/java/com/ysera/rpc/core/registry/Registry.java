package com.ysera.rpc.core.registry;

import java.util.List;

/**
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
     *  添加节点
     * @param key
     * @param value
     * @param deleteOnDisconnect
     */
    void put(String key, byte[] value, boolean deleteOnDisconnect);

    /**
     *  获取子节点
     * @param key
     * @return
     */
    List<String> children(String key);


    /**
     * 获取节点存储的信息
     * @param key
     * @return
     */
    byte[] getInfo(String key);

    /**
     *  判断节点是否存在
     * @param serviceName
     * @return
     */
    Boolean exist(String serviceName);
}
