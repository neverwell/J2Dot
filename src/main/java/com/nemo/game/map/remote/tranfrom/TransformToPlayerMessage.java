package com.nemo.game.map.remote.tranfrom;

import com.nemo.game.map.remote.TransformUtil;
import com.nemo.game.server.AbstractMessage;
import com.nemo.net.kryo.KryoInput;
import com.nemo.net.kryo.KryoOutput;

import java.util.ArrayList;
import java.util.List;

//发给玩家的消息.
//1.logic->map 这种情况一般pidList只有一个元素，具体体现在一些玩法，比如说走路什么的
//2.map->logic 这种情况一般pidList会有多个元素，具体体现在地图的视野同步等
public class TransformToPlayerMessage extends AbstractMessage {

    //玩家id列表
    private List<Long> pidList = new ArrayList<>();

    //消息id
    private int messageId;

    private byte[] messages;

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void doAction() {
        TransformUtil.dispatch(this);
    }

    @Override
    public boolean write(KryoOutput output) {
        writeShort(output, this.pidList.size());
        for (int i = 0; i < this.pidList.size(); i++) {
            this.writeLong(output, this.pidList.get(i));
        }
        this.writeInt(output, this.messageId, false);
        this.writeBytes(output, messages);
        return false;
    }

    @Override
    public boolean read(KryoInput input) {
        int length = readShort(input);
        for (int i = 0; i < length; i++) {
            this.pidList.add(this.readLong(input));
        }
        this.messageId = this.readInt(input, false);
        this.messages = this.readBytes(input);
        return false;
    }

    public List<Long> getPidList() {
        return pidList;
    }

    public void setPidList(List<Long> pidList) {
        this.pidList = pidList;
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
