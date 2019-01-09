package com.nemo.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message>{
    public MessageEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        ByteBuf buffer = null;

        try {
            if(msg.getId() == -1) {
                buffer = MessagePackage.packageMsgGroup(msg);
            } else {
                buffer = MessagePackage.packageMsg(msg);
            }

            if(buffer != null) {
                out.writeBytes(buffer);
            }
        } finally {
            if(buffer != null) {
                buffer.release();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
