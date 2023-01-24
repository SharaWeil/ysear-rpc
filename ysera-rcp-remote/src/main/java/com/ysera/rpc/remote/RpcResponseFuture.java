package com.ysera.rpc.remote;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @ClassName RpcResponseFuture
 * @createTIme 2023年01月19日 11:48:48
 **/
public class RpcResponseFuture {
    private final CountDownLatch latch = new CountDownLatch(1);

    private static final ConcurrentHashMap<Long, RpcResponseFuture> FUTURE_TABLE = new ConcurrentHashMap<>(256);

    private CallBack callBack;

    /**
     * timeout
     */
    private final long timeoutMillis;

    private volatile boolean sendOk = true;

    private Throwable throwable;

    private Response response;

    private long requestId;

    public RpcResponseFuture(long timeoutMillis,long requestId) {
        this.timeoutMillis = timeoutMillis;
        this.requestId = requestId;
        FUTURE_TABLE.put(requestId,this);
    }

    public RpcResponseFuture(long timeoutMillis,long requestId,CallBack callBack) {
        this.timeoutMillis = timeoutMillis;
        this.requestId = requestId;
        this.callBack = callBack;
        FUTURE_TABLE.put(requestId,this);
    }

    public boolean isSendOk() {
        return sendOk;
    }

    public void setSendOk(boolean sendOk) {
        this.sendOk = sendOk;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void putResponse(Response response) {
        this.response = response;
        latch.countDown();
        FUTURE_TABLE.remove(requestId);
    }

    public Response waitResponse() throws InterruptedException {
        latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return response;
    }


    /**
     * execute invoke callback
     */
    public void executeInvokeCallback() {
        if (callBack != null) {
            callBack.completed(response);
        }
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public static RpcResponseFuture getResponseFuture(long requestId){
        return FUTURE_TABLE.remove(requestId);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
