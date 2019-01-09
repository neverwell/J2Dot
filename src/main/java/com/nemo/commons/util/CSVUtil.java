package com.nemo.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtil.class);

    public static CSVData read(String filePath, int skipLine) {
        BufferedReader br = null;

        CSVData csvData;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            List<String> lines = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            csvData = new CSVData(lines, skipLine);
            return csvData;
        } catch (IOException var15) {
            LOGGER.error("csv文件读取出错:" + filePath, var15);
            csvData = null;
            return csvData;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException var14) {
                    LOGGER.error("文件关闭失败:" + filePath, var14);
                }
            }
        }
    }
}
