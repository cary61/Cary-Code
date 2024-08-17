package top.cary61.carycode.api.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Hash {

    public static String hash(String str) {
        return DigestUtils.sha256Hex(str);
    }
}
