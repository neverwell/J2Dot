package com.nemo.log.consumer.mysql;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
class TableDesc {
    private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("_yyyy_MM_dd");
    private static SimpleDateFormat YYYY_MM = new SimpleDateFormat("_yyyy_MM");
    private static SimpleDateFormat YYYY = new SimpleDateFormat("_yyyy");

    private TableCycle cycle;
    private String name;
    private String primaryKey;
    private String charset = TableCharset.UTF8MB4.value();

    //存放表字段定义
    private List<ColumnDesc> columns = new ArrayList<>();

    private int noAutoIncrementColumnCount;
    private String createSql; //创建表sql语句
    private String insertSql; //插入表sql语句

    public long time = System.currentTimeMillis();

    //模板
    //CREATE TABLE IF NOT EXISTS `p_bag` (
    //  `id` bigint(20) NOT NULL,
    //  `data` mediumblob,
    //  PRIMARY KEY (`id`)
    //  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
    private String buildCreateSql() {
        StringBuffer DDL = new StringBuffer();
        DDL.append("CREATE TABLE IF NOT EXISTS `%s` (\n");

        Iterator<ColumnDesc> iterator = this.columns.iterator();
        ColumnDesc col;
        while(iterator.hasNext()) {
            col = iterator.next();
            DDL.append(col.toDDL()).append(",\n");
        }

        iterator = this.columns.iterator();
        while(iterator.hasNext()) {
            col = iterator.next();
            if (col.isIndex()) {
                DDL.append(col.toIndex()).append(",\n");
            }
        }

        DDL.append("PRIMARY KEY (`" + this.primaryKey + "`))\n");
        DDL.append("ENGINE=InnoDB AUTO_INCREMENT=1 ");
        DDL.append("DEFAULT CHARSET=").append(this.charset);
        return DDL.toString();
    }

    private String buildInsertSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into `%s` ");
        StringBuilder fields = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");

        Iterator<ColumnDesc> iterator = this.columns.iterator();
        while(iterator.hasNext()) {
            ColumnDesc col = iterator.next();
            if (!col.isAutoIncrement()) {
                fields.append("`").append(col.getName()).append("`,");
                values.append("?,");
            }
        }

        if (fields.length() > 0) {
            fields.deleteCharAt(fields.length() - 1).append(")");
        }

        if (values.length() > 0) {
            values.deleteCharAt(values.length() - 1).append(")");
        }

        sql.append(fields).append(" values ").append(values);
        return sql.toString();
    }

    public Object[] buildInsertParam(Object log) {
        Object[] ret = new Object[this.noAutoIncrementColumnCount];
        int index = 0;

        try {
            Iterator<ColumnDesc> iterator = this.columns.iterator();
            while(iterator.hasNext()) {
                ColumnDesc col = iterator.next();
                if (!col.isAutoIncrement()) {
                    ret[index] = col.getReadMethod().invoke(log);
                    ++index;
                }
            }

            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取创建的表名
    public String buildName(long time) {
        switch(this.cycle) {
            case DAY:
                return this.name + YYYY_MM_DD.format(new Date(time));
            case MONTH:
                return this.name + YYYY_MM.format(new Date(time));
            case YEAR:
                return this.name + YYYY.format(new Date(time));
            case SINGLE:
                return this.name;
            default:
                return this.name;
        }
    }

    public void init() {
        int count = 0;
        Iterator<ColumnDesc> iterator = this.columns.iterator();
        while(iterator.hasNext()) {
            ColumnDesc col = iterator.next();
            if (!col.isAutoIncrement()) {
                ++count;
            }
        }
        this.noAutoIncrementColumnCount = count;

        this.createSql = this.buildCreateSql();
        this.insertSql = this.buildInsertSql();
    }

    public void addCol(ColumnDesc newCol) {
        for(int i = 0; i < this.columns.size(); ++i) {
            ColumnDesc col = this.columns.get(i);
            if (col.getName().equals(newCol.getName())) {
                this.columns.remove(i);
                this.columns.add(i, newCol);
                return;
            }
        }

        this.columns.add(newCol);
    }
}
