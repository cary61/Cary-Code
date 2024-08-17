package top.cary61.carycode.api.exception;

/**
 * 所有自定义异常的基类
 *
 */
public abstract class BaseException extends RuntimeException {



    // 错误码 (未启用)
    public long getCode() {
        return -1;
    }



    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
