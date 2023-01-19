package com.ysera.rpc.remote.protocol;

/**
 * @author admin
 * @ClassName RpcMessageHandler.java
 * @createTime 2023年01月17日 18:11:00
 *  ---------------------------------------------------------------------------------------
 *  ｜                                       protocol                                      ｜
 *  ---------------------------------------------------------------------------------------
 *  ｜ magic ｜  version  ｜  messageTyp ｜  compress   ｜ codec ｜RequestId ｜messageLength ｜
 *  ----------------------------------------------------------------------------------------
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ｜  4    ｜      1    ｜     1       ｜       1     ｜   1   ｜    8     ｜     4        ｜
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ----------------------------------------------------------------------------------------
 */
public class RpcHeader {

    private int magic;

    private byte version ;
    /**
     * rpc message type
     */
    private byte rpcType;
    /**
     * serialization type
     */
    private byte serialization;
    /**
     * compress type
     */
    private byte compress;
    /**
     * request id
     */
    private long requestId;

    private int bodyLength;

    public byte getRpcType() {
        return rpcType;
    }

    public void setRpcType(byte rpcType) {
        this.rpcType = rpcType;
    }

    public byte getSerialization() {
        return serialization;
    }

    public void setSerialization(byte serialization) {
        this.serialization = serialization;
    }

    public byte getCompress() {
        return compress;
    }

    public void setCompress(byte compress) {
        this.compress = compress;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }
}
