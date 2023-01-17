package com.ysera.rpc.remote.dto;

import com.ysera.rpc.enums.CodecType;
import com.ysera.rpc.enums.CompressType;
import com.ysera.rpc.enums.MessageType;

import java.io.Serializable;

/**
 * @author admin
 * @ClassName RpcMessage.java
 * @createTime 2023年01月17日 17:14:00
 *  ---------------------------------------------------------------------------------------
 *  ｜                                       protocol                                      ｜
 *  ---------------------------------------------------------------------------------------
 *  ｜ magic ｜  version  ｜  messageTyp ｜  compress   ｜ codec ｜RequestId ｜messageLength ｜
 *  ----------------------------------------------------------------------------------------
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ｜  4    ｜      1    ｜     1       ｜       1     ｜   1   ｜    4     ｜     4        ｜
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ----------------------------------------------------------------------------------------
 */
public class RpcMessage implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * rpc message type
     */
    private MessageType messageType;
    /**
     * serialization type
     */
    private CodecType codec;
    /**
     * compress type
     */
    private CompressType compressType;
    /**
     * request id
     */
    private int requestId;


    private int bodyLength;

    /**
     * request data
     */
    private byte[] body;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public CodecType getCodec() {
        return codec;
    }

    public void setCodec(CodecType codec) {
        this.codec = codec;
    }

    public CompressType getCompress() {
        return compressType;
    }

    public void setCompress(CompressType compress) {
        this.compressType = compress;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
