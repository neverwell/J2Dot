package com.nemo.game.util;

import com.nemo.game.processor.MessageSender;
import com.nemo.net.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class MessageUtil {

    private static MessageSender sender;

    public static void init(MessageSender sender) {
        MessageUtil.sender = sender;
    }


    public static void sendMsgToRids(Message msg, Collection<Long> rids) {

    }

    public static void sendToWorld(Message msg, int excludeHostId) {

    }


}
