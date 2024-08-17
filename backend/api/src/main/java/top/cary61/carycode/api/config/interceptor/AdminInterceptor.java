package top.cary61.carycode.api.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.entity.enumeration.Authority;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.commons.entity.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private Context context;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
                throws Exception {
        UserInfo userInfo = context.getUserInfo();
        // 不具有管理员权限
        if (userInfo == null || Objects.equals(userInfo.getAuthority(), Authority.USER)) {
            reject(response);
            return false;
        }
        return true;
    }

    private void reject(HttpServletResponse response) throws IOException {
        String userId;
        if (context.isLoggedIn()) {
            userId = Long.toString(context.getUserId());
        } else {
            userId = "null";
        }
        log.warn("非管理员访问管理接口！ 用户id: " + userId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(
                Result.fail("你没有管理员权限！")
        ));
    }
}
