package com.ysera.rpc.remote.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

import java.io.IOException;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public class KryoSerializer implements Serializer{

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return null;
    });
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        Kryo kryo = kryoThreadLocal.get();
        ByteBufferOutput output = new ByteBufferOutput();
        kryo.writeClassAndObject(output,obj);
        output.close();
        return output.toBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Kryo kryo = kryoThreadLocal.get();
        ByteBufferInput input = new ByteBufferInput(data);
        input.close();
        return kryo.readObject(input,clz);
    }
}
