package com.ysera.rpc.remote.serializer;

/**
 * @Author Administrator
 * @Date 2023/1/17
 **/
public interface Compress {


    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
