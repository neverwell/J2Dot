package com.nemo.game.entity.sys;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatBan {

    @Tag(1)
    private int time;

    @Tag(2)
    private String banReason;
}
