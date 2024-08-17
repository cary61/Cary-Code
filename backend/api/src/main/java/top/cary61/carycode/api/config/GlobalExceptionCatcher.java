package top.cary61.carycode.api.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.cary61.carycode.api.exception.*;
import top.cary61.carycode.commons.entity.Result;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionCatcher {

    @ExceptionHandler(Exception.class) // 捕获所有未被具体捕获的异常
    public Result<?> exceptionHandler(Exception e) {
        log.error("服务器内部错误！");
        e.printStackTrace();
        return Result.fail(StringUtils.defaultString(e.getMessage(), "请求失败了！"));
    }


    @ExceptionHandler(ParameterException.class)
    public Result<?> myParameterExceptionHandler(BaseException e) {
        return Result.fail(StringUtils.defaultString(e.getMessage(), "请求参数错误！"));
    }

    @ExceptionHandler(ServiceException.class)
    public Result<?> myServiceExceptionHandler(BaseException e) {
        return Result.fail(StringUtils.defaultString(e.getMessage(), "请求失败！"));
    }

    @ExceptionHandler(PersistenceException.class)
    public Result<?> myPersistenceExceptionHandler(BaseException e) {
        log.error("数据库异常！");
        e.printStackTrace();
        return Result.fail(StringUtils.defaultString(e.getMessage(), "请求未成功！"));
    }

    @ExceptionHandler(AuthorizationException.class)
    public Result<?> myAuthorizationExceptionHandler(BaseException e) {
        return Result.fail(StringUtils.defaultString(e.getMessage(), "没有权限！"));
    }




//    private String msg(String prefix, MyException e) {
//        return prefix + StringUtils.defaultString(e.getMessage());
//    }
//
//    private String msg(MyException e) {
//        return msg("", e);
//    }
}
