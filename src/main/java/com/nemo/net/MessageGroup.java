package com.nemo.net;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.concurrent.queue.ICommandQueue;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageGroup implements Message{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageGroup.class);
    private List<Message> messageList = new ArrayList<>();
    private int length;

    public void addMessage(Message message) {
        assert message != null;
        this.messageList.add(message);
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    @Override
    public byte[] encode() {
        byte[] bytes = null;
        Iterator<Message> iterator = this.messageList.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            ByteBuf byteBuf = null; //每个Message编码成ByteBuf

            try {
                if(message.getId() == -1) {
                    byteBuf = MessagePackage.packageMsgGroup(message);
                } else {
                    byteBuf = MessagePackage.packageMsg(message);
                }

                if(byteBuf != null) {
                    int bufLength = byteBuf.readableBytes();
                    if(bufLength > 0) {
                        byte[] curBytes = new byte[bufLength];
                        byteBuf.readBytes(curBytes); //ByteBuf的数据读到curBytes中

                        if(bytes != null && bytes.length > 0) {
                            byte[] oldBytes = bytes;
                            //bytes换成容积更大的数组
                            bytes = new byte[bytes.length + curBytes.length];

                            if(oldBytes.length > 0) {
                                //之前的数据移到新bytes中
                                System.arraycopy(oldBytes, 0, bytes, 0, oldBytes.length);
                            }
                            //现在数据接到新bytes中
                            System.arraycopy(curBytes, 0, bytes, oldBytes.length, curBytes.length);
                        } else {
                            bytes = curBytes;
                        }
                    }
                }
            } catch (Throwable e) {
                LOGGER.error("组消息编码错误", e);
            } finally {
                if(byteBuf != null) {
                    ReferenceCountUtil.release(byteBuf);
                }
            }
        }

        if(bytes == null) {
            bytes = new byte[0];
        }
        return bytes;
    }

    @Override
    public void setQueueId(int var1) {
    }

    @Override
    public ICommandQueue<IQueueDriverCommand> getCommandQueue() {
        return null;
    }

    @Override
    public void setCommandQueue(ICommandQueue<IQueueDriverCommand> var1) {
    }

    @Override
    public Object getParam() {
        return null;
    }

    @Override
    public void setParam(Object var1) {
    }

    @Override
    public void doAction() {
    }

    @Override
    public void run() {
    }

    @Override
    public void decode(byte[] bytes) {
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public int getQueueId() {
        return 0;
    }

    @Override
    public void setSequence(short var1) {
    }

    @Override
    public short getSequence() {
        return 0;
    }
}
