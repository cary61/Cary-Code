package top.cary61.carycode.api.util;

import java.util.concurrent.ThreadLocalRandom;

public class Random {

    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public static String suffix() {
        return Integer.toString(threadLocalRandom.nextInt(1000, 10000));
    }
}
