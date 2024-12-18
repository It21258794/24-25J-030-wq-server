package com.waterboard.waterqualityprediction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtils {
    public static Date nowAsDate() {
        return new Date();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static long toMilliseconds(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromMilliseconds(long value) {
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}