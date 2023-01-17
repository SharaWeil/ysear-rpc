package com.ysera.rpc.remote.code;

import com.ysera.rpc.remote.RpcConstants;
import com.ysera.rpc.remote.dto.RpcMessage;
import com.ysera.rpc.remote.procotol.KryoCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author admin
 * @ClassName NettyEncoder.java
 * @createTime 2023年01月17日 15:06:00
 *  ---------------------------------------------------------------------------------------
 *  ｜                                       protocol                                      ｜
 *  ---------------------------------------------------------------------------------------
 *  ｜ magic ｜  version  ｜  messageTyp ｜  compress   ｜ codec ｜RequestId ｜messageLength ｜
 *  ----------------------------------------------------------------------------------------
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ｜  5    ｜      1    ｜     1       ｜      1     ｜   1   ｜    4     ｜     4        ｜
 *  ｜       ｜           ｜             ｜             ｜       ｜          ｜              ｜
 *  ----------------------------------------------------------------------------------------
 *  不用压缩类型
 *
 */
public class NettyEncoder extends MessageToByteEncoder<RpcMessage> {

    private static final KryoCodec kryoCodec = new KryoCodec();


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage msg, ByteBuf out) throws Exception {
        if (null == msg){
            throw new RuntimeException("msg is null");
        }
        out.writeInt(RpcConstants.MAGIC_NUMBER);
        out.writeByte(RpcConstants.VERSION);
        out.writeByte(msg.getMessageType().ordinal());
        out.writeByte(msg.getCompress().ordinal());
        out.writeByte(msg.getCodec().ordinal());
        out.writeInt(msg.getRequestId());

        // write body
        out.writeInt(msg.getBodyLength());
        out.writeBytes(msg.getBody());
    }

}
