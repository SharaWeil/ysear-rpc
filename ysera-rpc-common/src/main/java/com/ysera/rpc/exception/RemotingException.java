package com.ysera.rpc.exception;

/**
 * @author admin
 * @ClassName RemotingException.java
 * @createTime 2023年01月17日 15:49:00
 */
public class RemotingException extends RuntimeException {
    public RemotingException(String msg) {
        super(msg);
    }
}
