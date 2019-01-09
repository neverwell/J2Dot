package com.nemo.game.system.email.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {
    //邮件id
    private long emailId;
    //玩家id
    private long ownerId;
    //邮件状态 0 未读 1 已读 2 已提取
    private int state;
    //标题
    private String title;
    //正文
    private String desc;
    //附件 ，空字符串代表没有附件
    private String items;
    //发送日期（以秒为单位的时间戳）
    private int time;
    //发送人
    private String sender;
    //邮件参数
    private int param;
    //邮件类型
    private int type;
    //数据库操作类型
    private transient int updateType;
}
