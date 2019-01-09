package com.nemo.net;

import com.nemo.concurrent.IQueueDriverCommand;

public interface Message extends IQueueDriverCommand{
    void decode(byte[] bytes);

    byte[] encode();

    int length();

    void setLength(int var1);

    int getId();

    int getQueueId();

    void setSequence(short var1);

    short getSequence();
}
