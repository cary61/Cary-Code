package top.cary61.carycode.commons.entity.enums;

public enum Storage {

    B(pow(1024, 0)),
    KB(pow(1024, 1)),
    MB(pow(1024, 2)),
    GB(pow(1024, 3)),
    TB(pow(1024, 4));

    public final long size;

    public final int intSize;

    Storage(long size) {
        this.size = size;
        this.intSize = (int)size;
    }

    private static long pow(long a, long n) {
        long ret = 1;
        for (int i = 0; i < n; i++) {
            ret *= a;
        }
        return ret;
    }
}
