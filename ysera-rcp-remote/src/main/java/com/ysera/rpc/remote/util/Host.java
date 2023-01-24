package com.ysera.rpc.remote.util;

import com.ysera.rpc.remote.protocol.RpcConstants;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

/*
 * @author Administrator
 * @ClassName Host
 * @createTIme 2023年01月19日 20:32:32
 **/
public class Host implements Serializable {

    public static final Host EMPTY = new Host();
    private static final String COLON = RpcConstants.COLON;

    /**
     * address
     */
    private String address;

    /**
     * ip
     */
    private String ip;

    /**
     * port
     */
    private int port;

    public Host() {
    }

    public Host(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.address = ip + COLON + port;
    }

    public Host(String address) {
        String[] parts = splitAddress(address);
        this.ip = parts[0];
        this.port = Integer.parseInt(parts[1]);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        String[] parts = splitAddress(address);
        this.ip = parts[0];
        this.port = Integer.parseInt(parts[1]);
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
        this.address = ip + COLON + port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        this.address = ip + COLON + port;
    }

    /**
     * address convert host
     *
     * @param address address
     * @return host
     */
    public static Host of(@NonNull String address) {
        String[] parts = splitAddress(address);
        return new Host(parts[0], Integer.parseInt(parts[1]));
    }

    /**
     * address convert host
     *
     * @param address address
     * @return host
     */
    public static String[] splitAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Host : address is null.");
        }
        String[] parts = address.split(COLON);
        if (parts.length != 2) {
            throw new IllegalArgumentException(String.format("Host : %s illegal.", address));
        }
        return parts;
    }

    /**
     * whether old version
     *
     * @param address address
     * @return old version is true , otherwise is false
     */
    public static Boolean isOldVersion(String address) {
        String[] parts = address.split(COLON);
        return parts.length != 2;
    }

    @Override
    public String toString() {
        return "Host{"
                + "address='" + address + '\''
                + ", ip='" + ip + '\''
                + ", port=" + port
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Host host = (Host) o;
        return port == host.port && Objects.equals(address, host.address) && Objects.equals(ip, host.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, ip, port);
    }
}
