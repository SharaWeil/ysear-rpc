package com.ysera.rpc.remote;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author admin
 * @ClassName RpcConstants.java
 * @createTime 2023年01月17日 17:36:00
 */
public class RpcConstants {
    /**
     * Magic number. Verify RpcMessage
     */
    public static final int MAGIC_NUMBER = 0x19f;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    //version information
    public static final byte VERSION = 1;
    public static final byte TOTAL_LENGTH = 16;
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    //ping
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    //pong
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    public static final int HEAD_LENGTH = 16;
    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}