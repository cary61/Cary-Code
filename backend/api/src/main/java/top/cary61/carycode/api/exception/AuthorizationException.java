package top.cary61.carycode.api.exception;

/**
 * 权限异常
 *
 * 代表着没有操作权限。
 * 如：修改他人的用户信息
 */
public class AuthorizationException extends BaseException {

    public AuthorizationException() {

    }

    public AuthorizationException(String message) {
        super(message);
    }
}
