package com.nemo.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by h on 2018/8/5.
 */
public class MapUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);

    public static long getMapKey(long mapId, long line) {
        return line << 32 | mapId;
    }

    public static byte[] readMapBytes(String binaryFile) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(new File(binaryFile));
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = fis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                fis.read(buffer);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("读取地图的二进制文件出错", e);
            return null;
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LOGGER.error("关闭文件输入流出错");
                }
            }
            if(bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    LOGGER.error("关闭字节数组输出流出错");
                }
            }
        }
    }
}
