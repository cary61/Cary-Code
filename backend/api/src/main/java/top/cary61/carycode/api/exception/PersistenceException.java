package top.cary61.carycode.api.exception;

/**
 * 数据库操作异常
 *
 * 代表着业务正确，但是数据库操作却发生错误
 * 如：登录时的密码错误、请求的数据不存在
 */
public class PersistenceException extends BaseException {
    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }
}
