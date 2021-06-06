package com.tao.cloud.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String getTime(LocalDateTime dateTime) {
        return dateTime.format(formatter1);
    }

    public static String getCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(formatter1);
    }

    public static String getTimeString() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(formatter2);
    }
}
