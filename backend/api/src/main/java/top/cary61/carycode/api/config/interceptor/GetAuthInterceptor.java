package top.cary61.carycode.api.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.cary61.carycode.api.config.Context;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class GetAuthInterceptor implements HandlerInterceptor {

    @Resource
    private Context context;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String method = request.getMethod();
        //  未登录
        if (!context.isLoggedIn()) {
            log.info("一条请求被拦截，原因：未通过 JWT Token 认证");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);   // 返回401
            return false;
        }
        return true;
    }
}
