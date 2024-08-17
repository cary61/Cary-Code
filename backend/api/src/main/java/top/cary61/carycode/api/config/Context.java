package top.cary61.carycode.api.config;

import org.springframework.stereotype.Component;
import top.cary61.carycode.api.entity.TokenObject;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.api.entity.po.UserState;
import top.cary61.carycode.api.mapper.UserInfoMapper;
import top.cary61.carycode.api.mapper.UserStateMapper;
import top.cary61.carycode.api.util.JWTUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class Context {

    @Resource
    private HttpServletRequest request;

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private UserInfoMapper userInfoMapper;


    private final ThreadLocal<TokenObject> tokenObject = new ThreadLocal<>();
    private final ThreadLocal<UserInfo> userInfo = new ThreadLocal<>();



    public TokenObject getTokenObject() {
        if (tokenObject.get() == null) {
            setContext();
        }
        return tokenObject.get();
    }

    private void setContext() {
        TokenObject tokenObject = jwtUtil.parseAndCheck(request.getHeader("Authorization"));
        this.tokenObject.set(tokenObject);
    }

    public Long getUserId() {
        if (!isLoggedIn()) {
            return null;
        }
        return getTokenObject().getUserId();
    }

    public UserInfo getUserInfo() {
        if (!isLoggedIn()) {
            return null;
        }
        if (userInfo.get() == null) {
            UserInfo userInfo = userInfoMapper.selectById(getUserId());
            this.userInfo.set(userInfo);
        }
        return userInfo.get();
    }

    public UserState getUserState() {
        if (!isLoggedIn()) {
            return null;
        }
        return getTokenObject().getUserState();
    }

    public boolean isLoggedIn() {
        return getTokenObject() != null;
    }

    public boolean loggedInAs(long userId) {
        return isLoggedIn() && getUserId() == userId;
    }

    public void clear() {
        tokenObject.remove();
        userInfo.remove();
    }
}
