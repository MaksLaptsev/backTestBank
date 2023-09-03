package com.maksru2009.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Преобразование {@link Timestamp} в читабельный вид
 */
public class StrToTimestamp {
    static Timestamp convert(String s) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return new Timestamp(df.parse(s).getTime());
    }
}
