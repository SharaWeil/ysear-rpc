package com.ysera.rpc.remote.procotol;

/**
 * @author admin
 * @ClassName Codec.java
 * @createTime 2023年01月17日 16:01:00
 */
public interface Codec {

    /**
     *  序列化
     * @param t
     * @return
     */
    <T> byte[] encoder(T t);


    /**
     *  反序列化
     * @param clazz 类型
     * @param bytes byte数组
     * @param <T>
     * @return
     */
    <T> T decoder(Class<T> clazz,byte[] bytes);
}
