package com.ysera.rpc.remote.serializer;

import java.io.IOException;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public interface Serializer {
    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
