package com.ysera.rpc.remote.serializer.compress;

import com.ysera.rpc.remote.serializer.Serializer;

import java.util.HashMap;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public enum RpcCompress {
    NONE((byte) 0, null);

    byte type;

    Compress compress;

    RpcCompress(byte type, Compress compress) {
        this.type = type;
        this.compress = compress;
    }

    public byte getType() {
        return type;
    }

    private static final HashMap<Byte, Compress> COMPRESS_MAP = new HashMap<>();

    static {
        for (RpcCompress compress : RpcCompress.values()) {
            COMPRESS_MAP.put(compress.type, compress.compress);
        }
    }

    public static Compress getCompressType(byte type) {
        return COMPRESS_MAP.get(type);
    }
}
