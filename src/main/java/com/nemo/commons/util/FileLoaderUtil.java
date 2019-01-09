package com.nemo.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileLoaderUtil {
    public FileLoaderUtil() {
    }

    public static URL findURLByFileName(String fileName) {
        ClassLoader cl = FileLoaderUtil.class.getClassLoader();
        return cl.getResource(fileName);
    }

    public static InputStream findInputStreamByFileName(String fileName) {
        try {
            URL url = findURLByFileName(fileName);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            return urlConnection.getInputStream();
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }


}
