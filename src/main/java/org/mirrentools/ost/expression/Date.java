package org.mirrentools.ost.expression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Date {
    static DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    static DateTimeFormatter   formatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static DateTimeFormatter   formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd");
    static Map<String, DateTimeFormatter> dateTimeFormatterMap = new ConcurrentHashMap<>();
    static {
        dateTimeFormatterMap.put("yyyyMMddHHmmssSSS", formatter);
        dateTimeFormatterMap.put("yyyyMMddHHmmss", formatter1);
        dateTimeFormatterMap.put("yyyyMMdd", formatter2);
    }
    public static String yyyyMMddHHmmssSSS(){
        return formatter.format(LocalDateTime.now());
    }
    public static String yyyyMMddHHmmss(){
        return formatter1.format(LocalDateTime.now());
    }
    public static String yyyyMMdd(){
        return formatter2.format(LocalDate.now());
    }
    public static String now(String pattern){
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterMap.computeIfAbsent(pattern, s -> DateTimeFormatter.ofPattern(pattern));
        return dateTimeFormatter.format(LocalDateTime.now());
    }

}
