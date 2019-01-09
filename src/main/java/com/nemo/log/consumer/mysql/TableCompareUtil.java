package com.nemo.log.consumer.mysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TableCompareUtil {
    private static Map<String, List<String>> FIELD_CHANGE_RULES = new HashMap<>();

    public static List<String> compare(String tableName, List<ColumnDesc> newColumns, List<ColumnDesc> oldColumns) throws Exception {
        Map<String, ColumnDesc> oldColumnMap = new HashMap<>();
        List<String> ret = new ArrayList<>();

        Iterator<ColumnDesc> iterator = oldColumns.iterator();
        ColumnDesc newCol;
        while(iterator.hasNext()) {
            newCol = iterator.next();
            oldColumnMap.put(newCol.getName(), newCol);
        }

        iterator = newColumns.iterator();
        while(true) {
            while(iterator.hasNext()) {
                newCol = iterator.next();
                ColumnDesc oldCol = oldColumnMap.get(newCol.getName());
                if (oldCol == null) {
                    ret.add("ALTER TABLE `" + tableName + "` ADD COLUMN " + newCol.toDDL() + ";");
                } else {
                    if (newCol.isIndex() && !oldCol.isIndex()) {
                        ret.add("ALTER TABLE `" + tableName + "` ADD INDEX `" + newCol.getIndexName() + "` (`" + newCol.getName() + "`);");
                    } else if (!newCol.isIndex() && oldCol.isIndex()) {
                        ret.add("ALTER TABLE `" + tableName + "` DROP INDEX `" + oldCol.getIndexName() + "`;");
                    }

                    if (!ableChange(newCol, oldCol)) {
                        throw new Exception(tableName + " " + newCol.toString() + " to " + oldCol + "列类型不匹配  无法自动变更");
                    }

                    String sql = getChangeSql(newCol, oldCol);
                    if (!sql.equals("")) {
                        ret.add("ALTER TABLE `" + tableName + "` MODIFY COLUMN " + sql + ";");
                    }
                }
            }

            return ret;
        }
    }

    public static String getChangeSql(ColumnDesc newColumn, ColumnDesc oldColumn) {
        return newColumn.getType().equals(oldColumn.getType()) && !sizeChanged(newColumn, oldColumn) && newColumn.isAllowNull() == oldColumn.isAllowNull() ? "" : newColumn.toDDL();
    }

    private static boolean sizeChanged(ColumnDesc newColumn, ColumnDesc oldColumn) {
        if (isNumberic(newColumn) && isNumberic(oldColumn)) {
            return false;
        } else {
            return newColumn.getSize() > oldColumn.getSize();
        }
    }

    private static boolean isNumberic(ColumnDesc column) {
        String type = column.getType();
        return type.equalsIgnoreCase(FieldType.TINYINT.name()) || type.equalsIgnoreCase(FieldType.SMALLINT.name()) || type.equalsIgnoreCase(FieldType.INT.name()) || type.equalsIgnoreCase(FieldType.BIT.name()) || type.equalsIgnoreCase(FieldType.BIGINT.name());
    }

    private static boolean ableChange(ColumnDesc newCol, ColumnDesc oldCol) {
        List<String> list = FIELD_CHANGE_RULES.get(newCol.getType());
        if (list == null) {
            return false;
        } else {
            return list.contains(oldCol.getType());
        }
    }

    static {
        List<String> bigintlist = new ArrayList<>();
        bigintlist.add("varchar");
        bigintlist.add("longtext");
        bigintlist.add("text");
        bigintlist.add("bigint");
        FIELD_CHANGE_RULES.put("bigint", bigintlist);
        List<String> bitlist = new ArrayList<>();
        bitlist.add("longtext");
        bitlist.add("varchar");
        bitlist.add("text");
        bitlist.add("bigint");
        bitlist.add("integer");
        bitlist.add("int");
        bitlist.add("int unsigend");
        bitlist.add("bit");
        FIELD_CHANGE_RULES.put("bit", bitlist);
        List<String> intlist = new ArrayList<>();
        intlist.add("longtext");
        intlist.add("varchar");
        intlist.add("text");
        intlist.add("bigint");
        intlist.add("integer");
        intlist.add("int");
        intlist.add("int unsigned");
        FIELD_CHANGE_RULES.put("int", intlist);
        FIELD_CHANGE_RULES.put("integer", intlist);
        List<String> shortlist = new ArrayList<>();
        shortlist.add("longtext");
        shortlist.add("varchar");
        shortlist.add("text");
        shortlist.add("bigint");
        shortlist.add("int");
        shortlist.add("integer");
        shortlist.add("short");
        FIELD_CHANGE_RULES.put("short", shortlist);
        List<String> bytelist = new ArrayList<>();
        bytelist.add("longtext");
        bytelist.add("varchar");
        bytelist.add("text");
        bytelist.add("bigint");
        bytelist.add("int");
        bytelist.add("short");
        bytelist.add("integer");
        FIELD_CHANGE_RULES.put("byte", bytelist);
        List<String> varcharlist = new ArrayList<>();
        varcharlist.add("longtext");
        varcharlist.add("varchar");
        varcharlist.add("text");
        varcharlist.add("int");
        varcharlist.add("bigint");
        FIELD_CHANGE_RULES.put("varchar", varcharlist);
        List<String> text = new ArrayList<>();
        text.add("longtext");
        text.add("text");
        text.add("varchar");
        FIELD_CHANGE_RULES.put("text", text);
        List<String> longtextlist = new ArrayList<>();
        longtextlist.add("longtext");
        FIELD_CHANGE_RULES.put("longtext", longtextlist);
        List<String> bloblist = new ArrayList<>();
        bloblist.add("blob");
        FIELD_CHANGE_RULES.put("blob", bloblist);
    }
}
