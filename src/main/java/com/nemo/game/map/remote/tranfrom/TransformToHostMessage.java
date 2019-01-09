package com.nemo.game.map.remote.tranfrom;

import com.nemo.game.map.remote.TransformUtil;
import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;


//转发给世界的消息 远程服务器的所有玩家
//logic->map 可能会排除掉自身host的玩家，因为该玩家在本服已经发送过一次
//map->logic
public class TransformToHostMessage extends AbstractMessage {

    //本服的hostId
    private int localhostId;

    //消息id
    private int messageId;

    private byte[] messages;

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void doAction() {
        TransformUtil.dispatch(this);
    }

    @Override
    public boolean write(KryoOutput output) {
        this.writeInt(output, this.localhostId, false);
        this.writeInt(output, this.messageId, false);
        this.writeBytes(output, messages);
        return false;
    }

    @Override
    public boolean read(KryoInput input) {
        this.localhostId = this.readInt(input, false);
        this.messageId = this.readInt(input, false);
        this.messages = this.readBytes(input);
        return false;
    }

    public int getLocalhostId() {
        return localhostId;
    }

    public void setLocalhostId(int localhostId) {
        this.localhostId = localhostId;
    }

    public byte[] getMessages() {
        return messages;
    }

    public void setMessages(byte[] messages) {
        this.messages = messages;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
