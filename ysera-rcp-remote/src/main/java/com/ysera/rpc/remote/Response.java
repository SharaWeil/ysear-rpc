package com.ysera.rpc.remote;

/*
 * @author Administrator
 * @ClassName Response
 * @createTIme 2023年01月19日 10:37:37
 **/
public class Response {
    private int version;

    private Object result;

    private Throwable throwable;

    private int requestId;


    public Response() {
    }

    public Response(int version, Object result, Throwable throwable, int requestId) {
        this.version = version;
        this.result = result;
        this.throwable = throwable;
        this.requestId = requestId;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
