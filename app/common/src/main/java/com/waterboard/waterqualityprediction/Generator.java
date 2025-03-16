package com.waterboard.waterqualityprediction;

import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

public class Generator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";

    public static int getRandom5DigitNumber() {
        return RandomUtils.nextInt(11111,95658);
    }

    public static String generateTemporaryPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RandomUtils.nextInt(0, ALPHABET.length()); // Corrected syntax
            password.append(ALPHABET.charAt(index));
        }
        return password.toString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
