package com.nemo.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class MessageDecoder extends LengthFieldBasedFrameDecoder{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
    private MessagePool msgPool;

    private MessageDecoder(MessagePool msgPool, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.msgPool = msgPool;
    }

    public MessageDecoder(MessagePool msgPool) throws IOException {
        this(msgPool, 1048576, 0, 4, -4, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if(frame == null) {
            return null;
        } else {
            Message msg;
            try {
                int length = frame.readInt();
                int id = frame.readInt();
                short sequence = frame.readShort();
                Message message = this.msgPool.get(id); //这里已经取出特定的子类Message
                if(message == null) {
                    return null;
                }

                byte[] bytes = null;
                int remainLength = frame.readableBytes();
                if(remainLength > 0) {
                    bytes = new byte[remainLength];
                    frame.readBytes(bytes);
                }

                message.setLength(length);
                message.setSequence(sequence);
                if(bytes != null) {
                    message.decode(bytes);
                }

                LOGGER.debug("解析消息：" + message);
                msg = message;
            } catch (Exception var14) {
                LOGGER.error(ctx.channel() + "消息解码异常", var14);
                return null;
            } finally {
                if(frame != null) {
                    ReferenceCountUtil.release(frame);
                }
            }

            return msg;
        }
    }
}
