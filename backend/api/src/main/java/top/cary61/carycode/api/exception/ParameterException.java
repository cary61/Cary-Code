package top.cary61.carycode.api.exception;

/**
 * 参数异常
 *
 * 代表着Controller参数缺失或不合法
 */
public class ParameterException extends BaseException {

    public ParameterException() {}

    public ParameterException(String message) {
        super(message);
    }
}
