package com.waterboard.waterqualityprediction;

import org.apache.commons.lang3.RandomUtils;

public class Generator {
    public static int getRandom5DigitNumber() {
        return RandomUtils.nextInt(11111,95658);
    }
}
