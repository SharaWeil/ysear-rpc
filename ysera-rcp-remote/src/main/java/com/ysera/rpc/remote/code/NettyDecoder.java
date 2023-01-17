package com.ysera.rpc.remote.code;

import com.ysera.rpc.enums.MessageType;
import com.ysera.rpc.remote.RpcConstants;
import com.ysera.rpc.remote.dto.RpcMessage;
import com.ysera.rpc.remote.dto.RpcMessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author admin
 * @ClassName NettyDecoder.java
 * @createTime 2023年01月17日 17:49:00
 */
public class NettyDecoder extends ReplayingDecoder<NettyDecoder.State> {

    private static final Logger logger = LoggerFactory.getLogger(NettyDecoder.class);

    public NettyDecoder() {
        super(State.MAGIC);
    }

    private final RpcMessageHeader messageHeader = new RpcMessageHeader();


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        switch (state()){
            case MAGIC:
                checkMagic(in.readInt());
                checkpoint(State.VERSION);
            case VERSION:
                checkVersion(in.readByte());
                checkpoint(State.MESSAGE_TYPE);
            case MESSAGE_TYPE:
                messageHeader.setMessageType(in.readByte());
                checkpoint(State.COMPRESS);
            case COMPRESS:
                messageHeader.setCompress(in.readByte());
                checkpoint(State.CODEC);
            case CODEC:
                messageHeader.setCodec(in.readByte());
                checkpoint(State.REQUEST_ID);
            case REQUEST_ID:
                messageHeader.setRequestId(in.readInt());
                checkpoint(State.BODY_LENGTH);
            case BODY_LENGTH:
                messageHeader.setBodyLength(in.readInt());
                checkpoint(State.BODY);
            case BODY:
                byte[] body = new byte[messageHeader.getBodyLength()];
                in.readBytes(body);
                RpcMessage message = new RpcMessage();
                message.setMessageType(messageType(messageHeader.getMessageType()));
                message.setBody(body);
                message.setBodyLength(messageHeader.getBodyLength());
//                message.setCodec(messageType(messageHeader.getCodec()));
                message.setCompress(message.getCompress());
                message.setRequestId(messageHeader.getRequestId());
                out.add(message);
                checkpoint(State.MAGIC);
                break;
            default:
                logger.warn("unknown decoder state {}", state());
        }
    }

    private void checkVersion(byte version) {
        if (version != RpcConstants.VERSION){
            throw new IllegalArgumentException("illegal packet [version]" + version);
        }
    }

    private void checkMagic(int magic) {
        if (magic != RpcConstants.MAGIC_NUMBER) {
            throw new IllegalArgumentException("illegal packet [magic]" + magic);
        }
    }

    private MessageType messageType(byte type) {
        for (MessageType ct : MessageType.values()) {
            if (ct.ordinal() == type) {
                return ct;
            }
        }
        return null;
    }


    enum State {
        MAGIC,
        VERSION,
        MESSAGE_TYPE,
        COMPRESS,
        CODEC,
        REQUEST_ID,
        BODY_LENGTH,
        BODY;
    }
}
