package com.ra.Utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static LocalTime parse(String text) {
        try {
            return LocalTime.parse(text.trim(), DateTimeFormatter.ofPattern("H:mm"));
        } catch (Exception e) {
            return null;
        }
    }

    public static int diffMinutes(LocalTime start, LocalTime end) {
        if (start == null || end == null) return 0;
        return (end.toSecondOfDay() - start.toSecondOfDay()) / 60;
    }

    public static int parseMinutes(String hm) {
        try {
            String[] s = hm.split(":");
            return Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
        } catch (Exception e) {
            return 0;
        }
    }
}
