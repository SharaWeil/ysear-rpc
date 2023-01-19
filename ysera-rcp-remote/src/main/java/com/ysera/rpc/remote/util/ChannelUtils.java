package com.ysera.rpc.remote.util;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/*
 * @author Administrator
 * @ClassName ChannelUtils
 * @createTIme 2023年01月19日 20:29:29
 **/
public class ChannelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtils.class);

    /**
     * get remote address
     *
     * @param channel channel
     * @return remote address
     */
    public static String getRemoteAddress(Channel channel) {
        return toAddress(channel).getAddress();
    }

    public static Host toAddress(Channel channel) {
        InetSocketAddress socketAddress = ((InetSocketAddress) channel.remoteAddress());
        if (socketAddress == null) {
            // the remote channel already closed
            LOGGER.warn("The channel is already closed");
            return Host.EMPTY;
        }
        return new Host(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
    }
}
