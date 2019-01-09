package com.nemo.game.system.miji.entity;

import io.protostuff.Tag;
import lombok.Data;

@Data
public class Skill {
    @Tag(1)
    private int skillId;

    @Tag(2)
    private int level = 0;

    @Tag(3)
    private int boundary = 0;
}
