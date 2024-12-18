package com.waterboard.waterqualityprediction;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class Check {
    public static void isTrue(boolean condition, RuntimeException exception) {
        if(!condition) throw exception;
    }

    public static void throwIfNull(Object object, RuntimeException exception) {
        isTrue(object != null, exception);
    }

    public static void throwIfBlank(String string, RuntimeException exception ) {
        isTrue(StringUtils.isNotBlank(string), exception);
    }

    public static void throwIfEmpty(Optional optional, RuntimeException exception) {
        if(optional.isEmpty() || optional.get() == null) {
            throw exception;
        }
    }

    public static void throwIfEmpty(ResultSet optional, RuntimeException exception) {
        if(optional.isEmpty() || optional.get() == null) {
            throw exception;
        }
    }
}
