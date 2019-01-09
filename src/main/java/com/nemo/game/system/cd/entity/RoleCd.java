package com.nemo.game.system.cd.entity;

import io.protostuff.Tag;

import java.util.HashMap;
import java.util.Map;

public class RoleCd {
    @Tag(1)
    private Map<Long, Cd> cdMap = new HashMap<>();
}
