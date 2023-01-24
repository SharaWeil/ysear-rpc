package com.ysera.rpc.core.registry;

import com.ysera.rpc.remote.serializer.Serializer;

import java.io.Serializable;

/**
 * @author admin
 * @ClassName RegistryInfo.java
 * @createTime 2023年01月24日 18:11:00
 */
public class RegistryInfo implements Serializable {
    private String clazz;

    private String serviceName;

    private int version;

    private String address;

    private int port;


    public RegistryInfo() {
    }

    public RegistryInfo(String clazz, String serviceName, int version) {
        this.clazz = clazz;
        this.serviceName = serviceName;
        this.version = version;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
