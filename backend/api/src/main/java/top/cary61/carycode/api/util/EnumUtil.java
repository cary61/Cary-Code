package top.cary61.carycode.api.util;

import top.cary61.carycode.commons.entity.enums.Language;

public class EnumUtil {

    public static<T extends Enum<T>> T parseAs(String name, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, name);
        } catch (Exception e) {
            return null;
        }
    }
}
