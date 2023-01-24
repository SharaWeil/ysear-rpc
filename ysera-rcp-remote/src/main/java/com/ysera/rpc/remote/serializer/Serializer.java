package com.ysera.rpc.remote.serializer;

import java.io.IOException;

/**
 * @Author Administrator
 * @Date 2023/1/17
 **/
public interface Serializer {
    /**
     * 把对象序列化为数组
     * @param obj 对象
     * @param <T>
     * @return 返回数组
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     *  把数组反序列化为对象
     * @param data
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
