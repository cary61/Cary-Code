package top.cary61.carycode.api.controller;

import org.springframework.web.bind.annotation.*;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.api.exception.AuthorizationException;
import top.cary61.carycode.api.exception.PersistenceException;
import top.cary61.carycode.api.exception.ServiceException;
import top.cary61.carycode.api.mapper.UserInfoMapper;
import top.cary61.carycode.commons.entity.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private Context context;

    @Resource
    private UserInfoMapper userInfoMapper;

    @GetMapping
    public Result<UserInfo> getUser(Long id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        if (userInfo == null) {
            throw new ServiceException("获取用户信息失败");
        }
        return Result.ok(userInfo);
    }

    @PutMapping
    public Result<?> updateUser(@RequestBody UserInfo userInfo) {
        requireAuthorization(userInfo);
        boolean b = userInfoMapper.updateById(userInfo) == 1;
        if (!b) {
            throw new PersistenceException("更新用户信息失败");
        }
        return Result.ok();
    }

    private void requireAuthorization(UserInfo userInfo) {
        boolean authorized = userInfo != null
                && userInfo.getId() != null
                && context.loggedInAs(userInfo.getId());
        if (!authorized) {
            throw new AuthorizationException("非法操作他人数据！");
        }
    }
}
