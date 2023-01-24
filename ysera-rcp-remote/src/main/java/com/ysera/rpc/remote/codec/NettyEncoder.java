package com.ysera.rpc.remote.codec;

import com.ysera.rpc.remote.protocol.RpcHeader;
import com.ysera.rpc.remote.protocol.RpcProtocol;
import com.ysera.rpc.remote.serializer.RpcSerializerType;
import com.ysera.rpc.remote.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author Administrator
 * @Date 2023/1/17
 *  ---------------------------------------------------------------------------------------
 *  ｜                                       protocol                                      ｜
 *  ---------------------------------------------------------------------------------------
 *  ｜ magic ｜  version  ｜  messageTyp ｜  compress   ｜ codec ｜RequestId ｜messageLength ｜
 *  ----------------------------------------------------------------------------------------
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ｜  4    ｜      1    ｜     1       ｜       1     ｜   1   ｜    4     ｜     4        ｜
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ----------------------------------------------------------------------------------------
 **/
public class NettyEncoder extends MessageToByteEncoder<RpcProtocol<?>> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol<?> msg, ByteBuf byteBuf) throws Exception {
        RpcHeader msgHeader = msg.getMsgHeader();
        byteBuf.writeInt(msgHeader.getMagic());
        byteBuf.writeByte(msgHeader.getVersion());
        byteBuf.writeByte(msgHeader.getRpcType());
        byteBuf.writeByte(msgHeader.getCompress());
        byteBuf.writeByte(msgHeader.getSerialization());
        byteBuf.writeLong(msgHeader.getRequestId());
        byte[] data = new byte[0];
        int msgLength = msgHeader.getBodyLength();
        Serializer rpcSerializer = RpcSerializerType.getSerializerByType(msgHeader.getSerialization());
        if (null != rpcSerializer) {
            data = rpcSerializer.serialize(msg.getBody());
            msgLength = data.length;
        }
        byteBuf.writeInt(msgLength);
        byteBuf.writeBytes(data);
    }
}
