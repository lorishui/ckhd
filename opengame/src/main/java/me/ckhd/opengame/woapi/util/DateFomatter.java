package me.ckhd.opengame.woapi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFomatter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static String format(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }
}
