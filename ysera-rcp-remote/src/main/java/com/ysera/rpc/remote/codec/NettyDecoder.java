package com.ysera.rpc.remote.codec;

import com.ysera.rpc.remote.protocol.RpcConstants;
import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import com.ysera.rpc.remote.protocol.RpcType;
import com.ysera.rpc.remote.serializer.RpcSerializerType;
import com.ysera.rpc.remote.serializer.Serializer;
import com.ysera.rpc.remote.serializer.Compress;
import com.ysera.rpc.remote.serializer.RpcCompressType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author Administrator
 * @Date 2023/1/17
 **/
public class NettyDecoder extends ByteToMessageDecoder {

    private final Class<?> genericClass;

    public NettyDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < RpcConstants.HEADER_LENGTH) {
            return;
        }

        byteBuf.markReaderIndex();

        int magic = byteBuf.readInt();

        if (RpcConstants.MAGIC_NUMBER != magic) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }
        byte version = byteBuf.readByte();
        byte eventType = byteBuf.readByte();
        byte compressType = byteBuf.readByte();
        byte serialization = byteBuf.readByte();
        long requestId = byteBuf.readLong();
        int dataLength = byteBuf.readInt();
        byte[] data = new byte[dataLength];

        RpcProtocol<Object> rpcProtocol = new RpcProtocol<>();

        RpcHeader header = new RpcHeader();
        header.setVersion(version);
        header.setRpcType(eventType);
        header.setCompress(compressType);
        header.setSerialization(serialization);
        header.setRequestId(requestId);
        header.setBodyLength(dataLength);
        byteBuf.readBytes(data);
        rpcProtocol.setMsgHeader(header);
        // 数据是否被压缩过
        Compress compress = RpcCompressType.getCompressType(compressType);
        if (compress != null){
            data = compress.decompress(data);
        }
        if (eventType != RpcType.HEARTBEAT.getType()) {
            Serializer serializer = RpcSerializerType.getSerializerByType(serialization);
            Object obj = serializer.deserialize(data, genericClass);
            rpcProtocol.setBody(obj);
        }
        list.add(rpcProtocol);
    }
}
