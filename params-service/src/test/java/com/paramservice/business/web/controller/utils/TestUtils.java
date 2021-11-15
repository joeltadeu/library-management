package com.paramservice.business.web.controller.utils;

import java.util.Random;

public class TestUtils {

    public static Long getRandomLong() {
        return Long.valueOf(Math.abs(new Random().nextInt()));
    }

    public static Integer getRandomInteger() {
        return Math.abs(new Random().nextInt());
    }

    public static String getRandomString() {
        return String.valueOf(getRandomInteger());
    }
}
