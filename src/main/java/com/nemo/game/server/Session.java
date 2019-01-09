package com.nemo.game.server;

import com.nemo.game.entity.Role;
import com.nemo.game.entity.User;
import com.nemo.net.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;

import java.net.InetSocketAddress;

//包装了Channel 表示一个连接
@Data
public class Session {
    private Channel channel;

    private User user;

    private Role role;

    private String djsChannel;

    private String params;

    //关闭连接
    public ChannelFuture close() {
        return channel.close();
    }

    //注册绑定一个User
    public void register(User user) {
        this.user = user;
    }

    public void registerRole(Role role) {
        this.role = role;
    }

    //是否已经注册
    public boolean isRegister() {
        return this.user != null;
    }

    //给客户端发送消息
    public void sendMessage(Message msg) {
        channel.writeAndFlush(msg);
    }

    //是否已经绑定Role
    public boolean isRoleRegister(){
        return role != null;
    }

    public String getIp() {
        if(channel == null) {
            return "";
        }
        return ((InetSocketAddress)channel.remoteAddress()).getHostString();
    }
}
