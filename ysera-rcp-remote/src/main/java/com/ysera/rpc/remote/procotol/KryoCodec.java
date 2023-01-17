package com.ysera.rpc.remote.procotol;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

/**
 * @author admin
 * @ClassName KryoCodec.java
 * @createTime 2023年01月17日 16:52:00
 */
public class KryoCodec implements Codec{

    private ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return null;
    });


    @Override
    public <T> byte[] encoder(T t) {
        Kryo kryo = kryoThreadLocal.get();
        ByteBufferOutput output = new ByteBufferOutput();
        kryo.writeClassAndObject(output,t);
        output.close();
        return output.toBytes();
    }

    @Override
    public <T> T decoder(Class<T> clazz, byte[] bytes) {
        Kryo kryo = kryoThreadLocal.get();
        ByteBufferInput input = new ByteBufferInput(bytes);
        input.close();
        return kryo.readObject(input,clazz);
    }
}
