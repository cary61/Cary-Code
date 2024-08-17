package top.cary61.carycode.api.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.entity.TokenObject;
import top.cary61.carycode.api.entity.po.InvalidToken;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.api.entity.po.UserState;
import top.cary61.carycode.api.exception.ParameterException;
import top.cary61.carycode.api.exception.PersistenceException;
import top.cary61.carycode.api.exception.ServiceException;
import top.cary61.carycode.api.mapper.InvalidTokenMapper;
import top.cary61.carycode.api.mapper.UserInfoMapper;
import top.cary61.carycode.api.mapper.UserStateMapper;
import top.cary61.carycode.api.service.AuthService;
import top.cary61.carycode.api.util.Hash;
import top.cary61.carycode.api.util.JWTUtil;
import top.cary61.carycode.commons.entity.Result;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserStateMapper userStateMapper;
    @Resource
    private InvalidTokenMapper invalidTokenMapper;

    @Resource
    private Context context;

    @Resource
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public Result<?> register(@RequestBody AuthDTO authDTO) {
        if (StringUtils.isBlank(authDTO.username)) {
            throw new ParameterException("用户名不能为空");
        }
        if (StringUtils.isBlank(authDTO.password)) {
            throw new ParameterException("密码不能为空");
        }
        if (userInfoMapper.selectByName(authDTO.username) != null) {
            throw new ParameterException("用户名已被注册，试试另一个名字吧！");
        }
        boolean b = authService.register(authDTO.username, authDTO.password);
        if (!b) {
            throw new PersistenceException("注册失败");
        }
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<AuthVO> login(@RequestBody AuthDTO authDTO) {
        UserInfo userInfo = userInfoMapper.selectByName(authDTO.username);
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }
        if (!authService.checkPassword(authDTO.getUsername(), authDTO.getPassword())) {
            throw new ServiceException("密码错误");
        }
        AuthVO authVO = new AuthVO();
        authVO.userInfo = userInfo;
        authVO.token = jwtUtil.encode(userInfo.getId());
        return Result.ok(authVO);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        TokenObject tokenObject = context.getTokenObject();
        if (tokenObject == null) {
            throw new ServiceException("token本就已经失效");
        }

        int count = invalidTokenMapper.insert(
                new InvalidToken(null, tokenObject.getUuid(), tokenObject.getExpiredTime())
        );
        if (count != 1) {
            throw new PersistenceException("退出登录失败");
        }
        return Result.ok();
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        UserState userState = context.getUserState();
        if (!authService.checkPassword(userState.getName(), updatePasswordDTO.oldPassword)) {
            throw new ServiceException("原密码错误！");
        }
        userState.setPasswordHash(Hash.hash(updatePasswordDTO.newPassword));
        userState.setPasswordUpdateTime(LocalDateTime.now());
        int count = userStateMapper.updateById(userState);
        if (count != 1) {
            throw new PersistenceException("密码更新失败");
        }
        return Result.ok();
    }

    @GetMapping("/user-info")
    public Result<UserInfo> getUserInfo() {
        UserInfo userInfo = context.getUserInfo();
        if (userInfo == null) {
            throw new PersistenceException("请求登录用户信息失败");
        }
        return Result.ok(userInfo);
    }

    @Data
    static class AuthDTO {
        String username;
        String password;
    }

    @Data
    static class AuthVO {
        UserInfo userInfo;

        // JWT token
        String token;
    }

    @Data
    static class UpdatePasswordDTO {
        String oldPassword;
        String newPassword;
    }
}
