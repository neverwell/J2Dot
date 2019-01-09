package com.nemo.game.notice;

//线程间通信
public abstract class ThreadModelProcessNotice extends ProcessNotice{
    @Override
    public int id() {
        return 0;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] bytes) {

    }
}
