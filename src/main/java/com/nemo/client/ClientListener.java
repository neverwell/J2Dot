package com.nemo.client;

import io.netty.channel.Channel;

public interface ClientListener {
    void afterConnected(Channel channel, int index);
}
