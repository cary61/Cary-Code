package top.cary61.carycode.api.entity.enumeration;

public enum Difficulty {

    NONE,  // 暂无评定
    EASY,
    MIDDLE,
    HARD

    ;

    /**
     * Returns the enum constant of the specified name
     *
     * @param name
     * @return null if {@code name} is null or not a valid name
     */
    public static Difficulty of(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }
}
