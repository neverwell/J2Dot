package com.nemo.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePackage.class);

    public MessagePackage(){
    }

    public static ByteBuf packageMsg(Message msg) {
        ByteBuf buffer = null;

        try {
            byte[] content = msg.encode();
            int length = 10 + content.length;
            msg.setLength(length);
            buffer = ByteBufAllocator.DEFAULT.buffer(length);
            buffer.writeInt(length);
            buffer.writeInt(msg.getId());
            buffer.writeShort(msg.getSequence());
            buffer.writeBytes(content);
            return buffer;
        } catch (Throwable t) {
            if(buffer != null) {
                ReferenceCountUtil.release(buffer);
            }

            LOGGER.error("消息编码错误：" + msg.getClass().getName(), t);
            return null;
        }
    }

    public static ByteBuf packageMsgGroup(Message msg) {
        byte[] content = msg.encode();
        int length = content.length;
        msg.setLength(length);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(length);
        buffer.writeBytes(content);
        return buffer;
    }
}
