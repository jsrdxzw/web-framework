package com.kyoshii.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Author: xuzhiwei
 * @Date: 2019-05-26
 * @Description: 流操作工具
 */
public final class StreamUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从输入流中获取字符串
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get string failure", e);
        }
        return sb.toString();
    }

    /**
     * 将输入流复制到输出流
     *
     * @param inputStream
     * @param outputStream
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) {
        try {
            int length;
            byte[] buffer = new byte[4 * 1024];
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.error("copy stream failure", e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                LOGGER.error("close stream failure", e);
            }
        }
    }

}
