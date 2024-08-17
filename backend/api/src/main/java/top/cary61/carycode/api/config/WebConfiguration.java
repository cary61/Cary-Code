package top.cary61.carycode.api.config;

import net.bytebuddy.asm.Advice;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import top.cary61.carycode.api.config.interceptor.AdminInterceptor;
import top.cary61.carycode.api.config.interceptor.AuthInterceptor;
import top.cary61.carycode.api.config.interceptor.BasicInterceptor;
import top.cary61.carycode.api.config.interceptor.GetAuthInterceptor;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    private BasicInterceptor basicInterceptor;
    @Resource
    private AuthInterceptor authInterceptor;
    @Resource
    private GetAuthInterceptor getAuthInterceptor;
    @Resource
    private AdminInterceptor adminInterceptor;

    /**
     * 非GET、非auth、并且需要从Context中获得UserInfo的请求，需要添加新registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 清理 Context, 防止数据污染
        registry.addInterceptor(basicInterceptor)
                .addPathPatterns("/**");

        // 拦截所有未登录状态下的非幂等请求(不包括登录、注册请求)
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/register")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/auth/logout");

        // 拦截部分 GET 请求
        registry.addInterceptor(getAuthInterceptor)
                        .addPathPatterns("/problem/search");

        // 访问后台管理接口时验证管理员权限
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        /**
         * 所有请求都允许跨域，使用这种配置就不需要
         * 在interceptor中配置header了
         */
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:5555", "http://localhost:5111")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowedHeaders("*")
                .maxAge(3600);
    }

}
