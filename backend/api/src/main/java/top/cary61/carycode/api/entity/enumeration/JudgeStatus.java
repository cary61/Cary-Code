package top.cary61.carycode.api.entity.enumeration;

public enum JudgeStatus {
    AC,
    WA,
    CE,
    RE,
    TLE,
    MLE

    ;

    /**
     * Returns the enum constant of the specified name
     *
     * @param name
     * @return null if {@code name} is null or not a valid name
     */
    public static JudgeStatus of(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }
}
