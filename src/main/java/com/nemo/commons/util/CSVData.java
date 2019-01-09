package com.nemo.commons.util;

import java.util.*;

public class CSVData {
    public String[] tableHead;
    public List<Map<String, String>> tableRows;
    private int skipLine;

    //通过readLine读取到的字符串列表
    public CSVData(List<String> lines, int skipLine) {
        this.skipLine = skipLine;
        this.skip(lines); //过滤掉前几行
        this.readTH(lines); //读取每个属性名
        this.readTR(lines); //读取每一条属性值 每一条属性值由一个Map 属性名 -> 属性值 表示

    }

    public void skip(List<String> lines) {
        for(int i = 0; i < this.skipLine && !lines.isEmpty(); i++) {
            lines.remove(0);
        }
    }

    public void readTH(List<String> lines) {
        String line = lines.remove(0);
        this.tableHead = line.trim().split(",");
    }

    public void readTR(List<String> lines) {
        this.tableRows = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++) {
            Map<String, String> tr = new HashMap<>();
            String line = lines.get(i);
            String[] lineArray = line.split(",");

            for(int j = 0; j < lineArray.length; j++) {
                if(j < this.tableHead.length) {
                    String col = lineArray[j]; //属性值
                    tr.put(tableHead[j], col);
                }
            }
            this.tableRows.add(tr);
        }
    }

    public String toString() {
        return "ListConfigData:" + Arrays.toString(this.tableHead) + "=>" + this.tableRows.toString();
    }
}
