package top.cary61.carycode.api.entity.enumeration;

public enum Authority {
    USER,

    MANAGER,

    ADMINISTRATOR

    ;

    /**
     * Returns the enum constant of the specified name
     *
     * @param name
     * @return null if {@code name} is null or not a valid name
     */
    public static Authority of(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }
}
