package top.cary61.carycode.api.exception;

/**
 * 业务异常
 *
 * 代表着业务流程失败
 */
public class ServiceException extends BaseException {

    public ServiceException() {

    }


    public ServiceException(String message) {
        super(message);
    }
}
