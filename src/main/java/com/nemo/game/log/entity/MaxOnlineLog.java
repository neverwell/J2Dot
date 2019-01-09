package com.nemo.game.log.entity;

import com.nemo.game.util.IDUtil;
import com.nemo.game.util.TimeUtil;
import com.nemo.log.AbstractLog;
import com.nemo.log.consumer.mysql.FieldType;
import com.nemo.log.consumer.mysql.TableCycle;
import com.nemo.log.consumer.mysql.annotation.Column;
import com.nemo.log.consumer.mysql.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Table(cycle = TableCycle.SINGLE, tableName = "log_max_online", primaryKey = "id")
@Getter
@Setter
public class MaxOnlineLog extends AbstractLog{

    @Column(fieldType = FieldType.BIGINT, size = 20, commit = "日志ID", colName = "id")
    private long id;

    @Column(fieldType = FieldType.INT, size = 11, commit = "在线人数", colName = "num")
    private int num;

    @Column(fieldType = FieldType.INT, size = 11, commit = "time", colName = "time")
    private int time;

    @Column(fieldType = FieldType.INT, size = 11, commit = "平台id", colName = "pid")
    private int pid;

    @Column(fieldType = FieldType.INT, size = 11, commit = "区服id", colName = "sid")
    private int sid;

    @Column(fieldType = FieldType.INT, size = 11, commit = "渠道", colName = "qudao")
    private int qudao;

    public MaxOnlineLog() {
        this.id = IDUtil.getId();
        this.time = TimeUtil.getNowOfSeconds();
    }
}
