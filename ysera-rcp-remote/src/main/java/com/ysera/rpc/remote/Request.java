package com.ysera.rpc.remote;

import java.lang.reflect.Method;

/*
 * @author Administrator
 * @ClassName Request
 * @createTIme 2023年01月19日 10:24:24
 **/
public class Request{
    private String clazzName;

    private Method method;

    private Object[] arguments;

    private Class<?>[] paramType;



    private int version;

    public Request() {
    }


    public Request(String clazzName, Method method, Object[] arguments, Class<?>[] paramType, int version) {
        this.clazzName = clazzName;
        this.method = method;
        this.arguments = arguments;
        this.paramType = paramType;
        this.version = version;
    }

    public String getClazzName() {
        return clazzName;
    }
    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Class<?>[] getParamType() {
        return paramType;
    }

    public void setParamType(Class<?>[] paramType) {
        this.paramType = paramType;
    }
}
