package com.nemo.game.map.remote.tranfrom;

import com.nemo.game.map.remote.TransformUtil;
import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;


//玩家请求从游戏服转发到地图服
public class TransformToMapMessage extends AbstractMessage {

    //玩家playerId
    private long pid;

    //转发的消息的id
    private int messageId;

    //转发的消息真实内容
    private byte[] messages;

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void doAction() {
        TransformUtil.dispatch(this);
    }

    @Override
    public boolean write(KryoOutput output) {
        this.writeLong(output, this.pid);
        this.writeInt(output, this.messageId, false);
        this.writeBytes(output, messages);
        return false;
    }

    @Override
    public boolean read(KryoInput input) {
        this.pid = readLong(input);
        this.messageId = readInt(input, false);
        this.messages = this.readBytes(input);
        return false;
    }

    public byte[] getMessages() {
        return messages;
    }

    public void setMessages(byte[] messages) {
        this.messages = messages;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
