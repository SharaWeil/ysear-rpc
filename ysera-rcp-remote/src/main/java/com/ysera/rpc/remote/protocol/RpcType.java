package com.ysera.rpc.remote.protocol;

import com.ysera.rpc.remote.serializer.Compress;
import com.ysera.rpc.remote.serializer.Serializer;

import java.util.HashMap;

/*
 * @Author Administrator
 * @Date 2023/1/17
 **/
public enum RpcType {
    HEARTBEAT((byte)1,"heartbeat"),
    REQUEST((byte)2,"request"),
    RESPONSE((byte)3,"response");

    private final Byte type;

    private final String description;

    RpcType(Byte type, String description) {
        this.type = type;
        this.description = description;
    }

    public Byte getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static RpcType getRpcType(byte type) {
        return RpcType.values()[type-1];
    }
}
