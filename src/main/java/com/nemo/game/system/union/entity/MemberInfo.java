package com.nemo.game.system.union.entity;

import io.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

//帮会成员的一些信息
@Getter
@Setter
public class MemberInfo {

    //成员id
    @Tag(1)
    long memberId;

    //职位
    @Tag(2)
    int position;
}
