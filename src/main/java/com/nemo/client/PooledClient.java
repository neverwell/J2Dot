package com.nemo.client;

import io.netty.channel.Channel;

public class PooledClient extends Client{
    protected Channel[] channels;
    protected int[] reconnectDelayArray;

    public PooledClient(ClientBuilder builder) {

    }


}
