package com.ysera.rpc.remote.serializer;

import com.ysera.rpc.remote.util.KryoUtils;

import java.io.*;

/**
 * @Author Administrator
 * @Date 2023/1/17
 **/
public class KryoSerializer implements Serializer{

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return KryoUtils.serialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return KryoUtils.deserialize(data,clz);
    }
}
