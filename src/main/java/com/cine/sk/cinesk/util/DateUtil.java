package com.cine.sk.cinesk.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
    public static LocalDateTime extractFirstDate(String text) {
        String datePattern = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String dateStr = matcher.group();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return LocalDateTime.parse(dateStr, formatter);
        }
        return null;
    }
}
