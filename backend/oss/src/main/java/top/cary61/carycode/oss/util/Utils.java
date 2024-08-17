package top.cary61.carycode.oss.util;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    private static final String[] hex = {
            "0", "1", "2", "3",
            "4", "5", "6", "7",
            "8", "9", "A", "B",
            "C", "D", "E", "F"
    };

    public static String hash() {
        long value = threadLocalRandom.nextLong();
        return getHexString(value);
    }

    private static String getHexString(long value) {
        int mask = 0b1111;
        StringBuilder sb = new StringBuilder();
        for (int i = 15; i >= 0; i--) {
            int field = (int)(value >> i * 4) & mask;
            sb.append(hex[field]);
        }
        return sb.toString();
    }
}
