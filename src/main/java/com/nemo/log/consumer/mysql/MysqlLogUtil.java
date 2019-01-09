package com.nemo.log.consumer.mysql;

import com.nemo.common.jdbc.JdbcTemplate;
import com.nemo.log.AbstractLog;
import com.nemo.log.consumer.mysql.annotation.Column;
import com.nemo.log.consumer.mysql.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MysqlLogUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(MysqlLogUtil.class);
    private static final Map<Class<?>, TableDesc> tableDescMap = new HashMap<>();

    //解析AbstractLog子类 生成表
    public static void parse(JdbcTemplate template, AbstractLog log) throws Exception {
        Class<?> clazz = log.getClass();
        Table table = clazz.getAnnotation(Table.class);
        if(table != null) {
            //获取Table注解定义
            TableDesc tableDesc = new TableDesc();
            tableDesc.setCycle(table.cycle());
            String tableName = table.tableName();
            if(tableName == null || tableName.equals("")) {
                tableName = clazz.getSimpleName();
            }
            tableDesc.setName(tableName);
            tableDesc.setPrimaryKey(table.primaryKey());
            tableDesc.setCharset(table.charset().value());

            //获取log类的所有父类 获取所有字段注解
            List<Class<?>> clazzList = new ArrayList<>();
            for(; clazz.getSuperclass() != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
                clazzList.add(0, clazz);
            }

            Iterator<Class<?>> iterator = clazzList.iterator();
            while (iterator.hasNext()) {
                Class<?> cl = iterator.next();
                Field[] fields = cl.getDeclaredFields();

                for(int i = 0; i < fields.length; ++i) {
                    Field field = fields[i];
                    Column column = field.getAnnotation(Column.class);
                    if(column != null) {
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cl);
                        Method readMethod = pd.getReadMethod();
                        if(readMethod != null) {
                            //获取每个字段注解定义
                            ColumnDesc colDesc = new ColumnDesc();
                            colDesc.setReadMethod(readMethod);
                            String colName = column.colName();
                            if(colName == null || colName.equals("")) {
                                colName = field.getName();
                            }
                            colDesc.setName(colName.toLowerCase());
                            colDesc.setType(column.fieldType().name().toLowerCase()); //数据类型
                            colDesc.setSize(column.size());
                            colDesc.setAllowNull(column.allowNull());
                            colDesc.setAutoIncrement(column.autoIncrement());
                            colDesc.setCommit(column.commit());
                            colDesc.setIndex(!column.index().equals(""));
                            colDesc.setIndexName(column.index());
                            tableDesc.addCol(colDesc);
                        }
                    }
                }
            }

            //创建生成和插入sql语句
            tableDesc.init();

            tableDescMap.put(log.getClass(), tableDesc);
            checkTable(template, log);
        }
    }

    private static void checkTable(JdbcTemplate template, AbstractLog log) throws Exception {
        String buildName = tableDescMap.get(log.getClass()).buildName(System.currentTimeMillis());
        LOGGER.info("检测查表" + buildName);

        Connection connection = null;
        Statement statement = null;
        try {
            connection = template.getPool().getConnection();
            List<String> tableNames = getTableName(connection);
            if (!tableNames.contains(buildName)) {
                if (tableDescMap.get(log.getClass()).getCycle() == TableCycle.SINGLE) {
                    String createSql = buildCreateSql(log);
                    try {
                        template.update(createSql, new Object[0]);
                    } catch (Exception e) {
                        LOGGER.error("创建日志表报错", e);
                    }
                }
            } else {
                List<ColumnDesc> columnDefine = getColumnDefine(connection, buildName);
                List<String> primaryKeys = getTablePrimaryKeys(connection, buildName);

                Iterator<ColumnDesc> iterator = columnDefine.iterator();
                while(iterator.hasNext()) {
                    ColumnDesc next = iterator.next();
                    if (primaryKeys.contains(next.getName())) {
                        iterator.remove();
                    }
                }

                List<ColumnDesc> newColumns = new ArrayList<>();
                Iterator<ColumnDesc> iterator1 = tableDescMap.get(log.getClass()).getColumns().iterator();
                while(iterator1.hasNext()) {
                    ColumnDesc col = iterator1.next();
                    if (!tableDescMap.get(log.getClass()).getPrimaryKey().equals(col.getName())) {
                        newColumns.add(col);
                    }
                }

                List<String> comparator = TableCompareUtil.compare(buildName, newColumns, columnDefine);
                if (comparator.size() > 0) {
                    statement = connection.createStatement();
                    Iterator<String> iterator2 = comparator.iterator();
                    while(iterator2.hasNext()) {
                        String string = iterator2.next();
                        LOGGER.info("检查到变更：" + string);
                        statement.addBatch(string);
                    }
                    statement.executeBatch();
                }
            }
            LOGGER.info(buildName + "检查结束");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOGGER.error("关闭statement失败", e);
                }
            }
            if (connection != null) {
                template.getPool().release(connection);
            }
        }
    }

    private static List<ColumnDesc> getColumnDefine(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet columns = metaData.getColumns(null, "%", tableName, "%");

        List<ColumnDesc> infos = new ArrayList<>();
        while(columns.next()) {
            ColumnDesc info = new ColumnDesc();
            info.setName(columns.getString("COLUMN_NAME").toLowerCase());
            info.setType(columns.getString("TYPE_NAME").toLowerCase());
            info.setSize(columns.getInt("COLUMN_SIZE"));
            info.setAllowNull(columns.getBoolean("IS_NULLABLE"));
            infos.add(info);
        }

        ResultSet indexes = metaData.getIndexInfo(null, "%", tableName, false, false);
        while(true) {
            while(indexes.next()) {
                String indexName = indexes.getString("INDEX_NAME");
                String columnName = indexes.getString("COLUMN_NAME");

                Iterator<ColumnDesc> iterator = infos.iterator();
                while(iterator.hasNext()) {
                    ColumnDesc info = iterator.next();
                    if (info.getName().equals(columnName)) {
                        info.setIndex(true);
                        info.setIndexName(indexName);
                        break;
                    }
                }
            }
            return infos;
        }
    }

    private static List<String> getTablePrimaryKeys(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getPrimaryKeys(null, "%", tableName);
        List<String> ret = new ArrayList<>();
        while(rs.next()) {
            ret.add(rs.getString("COLUMN_NAME"));
        }
        return ret;
    }

    //数据库的所有表名
    private static List<String> getTableName(Connection conn) throws SQLException {
        ResultSet tableRet = conn.getMetaData().getTables(null, null, null, null);

        List<String> tableNameList = new ArrayList<>();
        while(tableRet.next()) {
            tableNameList.add(tableRet.getString("TABLE_NAME"));
        }
        return tableNameList;
    }

    public static String buildCreateSql(AbstractLog log) {
        return String.format(tableDescMap.get(log.getClass()).getCreateSql(), tableDescMap.get(log.getClass()).buildName(System.currentTimeMillis()));
    }

    public static String buildInsertSQL(AbstractLog log) {
        return String.format(tableDescMap.get(log.getClass()).getInsertSql(), tableDescMap.get(log.getClass()).buildName(System.currentTimeMillis()));
    }

    public static Object[] buildInsertParam(AbstractLog log) {
        return tableDescMap.get(log.getClass()).buildInsertParam(log);
    }
}
