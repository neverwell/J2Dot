package com.nemo.game.map.remote;

import com.nemo.concurrent.IQueueDriverCommand;
import com.nemo.concurrent.queue.ICommandQueue;
import com.nemo.game.map.remote.tranfrom.TransformToPlayerMessage;
import com.nemo.net.Message;

public class TransformMessageGroup implements Message {

    private int length = 0;

    byte[] bytes;

    public TransformMessageGroup(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] encode() {
        if(bytes == null || bytes.length == 0) {
            return null;

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
