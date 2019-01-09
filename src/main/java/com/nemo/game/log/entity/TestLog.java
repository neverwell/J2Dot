package com.nemo.game.log.entity;

import com.nemo.log.AbstractLog;
import com.nemo.log.consumer.mysql.FieldType;
import com.nemo.log.consumer.mysql.TableCycle;
import com.nemo.log.consumer.mysql.annotation.Column;
import com.nemo.log.consumer.mysql.annotation.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Table(tableName = "log_test", primaryKey = "id", cycle = TableCycle.SINGLE)
@Data
public class TestLog extends AbstractLog{
    @Column(allowNull = false, fieldType = FieldType.INT, size = 11, commit = "日志ID", colName = "id", autoIncrement = true)
    private int id;

    @Column(fieldType = FieldType.INT, size = 11, commit = "服务器id")
    private int serverId;

    @Column(fieldType = FieldType.VARCHAR, size = 64, commit = "道具名称")
    private String name;

    @Column(fieldType = FieldType.INT, size = 11, commit = "时间")
    private long time;

    @Column(fieldType = FieldType.INT, size = 11, commit = "道具ID")
    private int itemId;

    @Column(fieldType = FieldType.INT, size = 11, commit = "数量")
    private int num;

    @Column(fieldType = FieldType.VARCHAR, size = 256, allowNull = true, commit = "触发动作")
    private String action;
}
