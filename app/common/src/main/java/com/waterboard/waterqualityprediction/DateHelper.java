package com.waterboard.waterqualityprediction;

import java.time.LocalDateTime;
import java.util.Date;

public class DateHelper {

    public enum Type {
        HOURS, DAYS, MINUTES, SECONDS
    }

    public static Date nowAsDate() {
        return new Date();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
