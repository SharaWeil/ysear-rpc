package com.ysera.rpc.remote.protocol;

/**
 * @Author Administrator
 * @Date 2023/1/17
 **/
public class RpcProtocol<T> {
    private RpcHeader msgHeader;

    private T body;

    public RpcHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(RpcHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "msgHeader=" + msgHeader +
                ", body=" + body +
                '}';
    }
}
