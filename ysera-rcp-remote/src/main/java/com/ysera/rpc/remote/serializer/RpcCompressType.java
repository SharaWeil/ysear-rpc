package com.ysera.rpc.remote.serializer;

import java.util.HashMap;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public enum RpcCompressType {
    NONE((byte) 0, null);

    byte type;

    Compress compress;

    RpcCompressType(byte type, Compress compress) {
        this.type = type;
        this.compress = compress;
    }

    public byte getType() {
        return type;
    }

    private static final HashMap<Byte, Compress> COMPRESS_MAP = new HashMap<>();

    static {
        for (RpcCompressType compress : RpcCompressType.values()) {
            COMPRESS_MAP.put(compress.type, compress.compress);
        }
    }

    public static Compress getCompressType(byte type) {
        return COMPRESS_MAP.get(type);
    }
}
