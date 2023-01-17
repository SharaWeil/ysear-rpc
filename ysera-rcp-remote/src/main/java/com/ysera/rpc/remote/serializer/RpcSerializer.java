package com.ysera.rpc.remote.serializer;

import java.util.HashMap;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public enum RpcSerializer {
    KRYO((byte) 1, new KryoSerializer());

    byte type;

    Serializer serializer;

    RpcSerializer(byte type, Serializer serializer) {
        this.type = type;
        this.serializer = serializer;
    }

    public byte getType() {
        return type;
    }

    private static final HashMap<Byte, Serializer> SERIALIZERS_MAP = new HashMap<>();

    static {
        for (RpcSerializer rpcSerializer : RpcSerializer.values()) {
            SERIALIZERS_MAP.put(rpcSerializer.type, rpcSerializer.serializer);
        }
    }

    public static Serializer getSerializerByType(byte type) {
        return SERIALIZERS_MAP.get(type);
    }
}
