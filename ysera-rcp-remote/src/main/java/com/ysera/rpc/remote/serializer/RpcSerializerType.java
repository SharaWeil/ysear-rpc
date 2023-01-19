package com.ysera.rpc.remote.serializer;

import java.util.HashMap;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public enum RpcSerializerType {
    KRYO((byte) 1, new KryoSerializer());

    byte type;

    Serializer serializer;

    RpcSerializerType(byte type, Serializer serializer) {
        this.type = type;
        this.serializer = serializer;
    }

    public byte getType() {
        return type;
    }

    private static final HashMap<Byte, Serializer> SERIALIZERS_MAP = new HashMap<>();

    static {
        for (RpcSerializerType rpcSerializerType : RpcSerializerType.values()) {
            SERIALIZERS_MAP.put(rpcSerializerType.type, rpcSerializerType.serializer);
        }
    }

    public static Serializer getSerializerByType(byte type) {
        return SERIALIZERS_MAP.get(type);
    }
}
