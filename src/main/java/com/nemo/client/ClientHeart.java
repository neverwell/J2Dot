package com.nemo.client;

import com.nemo.net.Message;

public class ClientHeart implements Runnable{
    private ClientHeart.PingMessageFactory pingMessageFactory;
    private Client client;

    public ClientHeart(ClientHeart.PingMessageFactory pingMessageFactory, Client client) {
        this.pingMessageFactory = pingMessageFactory;
        this.client = client;
    }

    @Override
    public void run() {
        if(this.client.connected) {
            Message ping = this.pingMessageFactory.getPingMessage();
            this.client.ping(ping);
        }
    }

    public interface PingMessageFactory {
        Message getPingMessage();
    }
}
