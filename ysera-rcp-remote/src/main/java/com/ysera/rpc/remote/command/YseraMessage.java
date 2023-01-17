package com.ysera.rpc.remote.command;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author admin
 * @ClassName YseraMessage.java
 * @createTime 2023年01月17日 15:08:00
 */
public class YseraMessage implements Serializable {
    private static final long serialVersionUID = -1L;

    private static final AtomicLong REQUEST_ID = new AtomicLong(1);

    public static final byte MAGIC = (byte) 0xbabe;
    public static final byte VERSION = 0;

    private byte[] body;

    private MessageType type;

    public YseraMessage(){

    }

    public static AtomicLong getRequestId() {
        return REQUEST_ID;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
