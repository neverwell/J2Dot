package com.nemo.log.consumer.mysql;

import com.nemo.common.jdbc.JdbcTemplate;
import com.nemo.log.AbstractLog;
import com.nemo.log.consumer.DefaultConsumerType;
import com.nemo.log.consumer.LogConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MysqlLogConsumer implements LogConsumer{
    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlLogConsumer.class);
    //启动服务器时用来读取AbstractLog类型的java文件动态生成对应的表
    private JdbcTemplate template;

    public MysqlLogConsumer(JdbcTemplate template) throws Exception {
        this.template = template;
    }

    //执行插入操作
    @Override
    public void consumer(AbstractLog log) {
        String insertSql = MysqlLogUtil.buildInsertSQL(log);
        Object[] params = MysqlLogUtil.buildInsertParam(log);

        try {
            int ret = this.template.update(insertSql, params);
            if(ret < 0) {
                String createSql = MysqlLogUtil.buildCreateSql(log);
                this.template.update(createSql, new Object[0]);
                this.template.update(insertSql, params);
            }
        } catch (Exception e) {
            LOGGER.error("日志入库执行失败sql:" + insertSql + ",params:" + Arrays.toString(params), e);
        }
    }

    @Override
    public int type() {
        return DefaultConsumerType.MYSQL;
    }

    @Override
    public void parse(AbstractLog log) throws Exception {
        MysqlLogUtil.parse(this.template, log);
    }
}
