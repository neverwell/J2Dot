package com.nemo.net;

import io.netty.channel.Channel;

public interface NetworkConsumer {
    void consume(Channel var1, Message var2);
}
