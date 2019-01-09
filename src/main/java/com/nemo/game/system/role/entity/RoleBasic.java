package com.nemo.game.system.role.entity;

import io.protostuff.Tag;
import lombok.Data;

@Data
public class RoleBasic {
    //角色id，全服唯一性
    @Tag(1)
    private long id;
    //角色名，全服唯一角色名
    @Tag(2)
    private String name;

}
