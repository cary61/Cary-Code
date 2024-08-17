package top.cary61.carycode.api.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cary61.carycode.api.entity.UserDO;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.api.entity.po.UserState;
import top.cary61.carycode.api.mapper.UserInfoMapper;
import top.cary61.carycode.api.mapper.UserStateMapper;
import top.cary61.carycode.commons.entity.Result;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserStateMapper userStateMapper;

    @GetMapping("/hello")
    public Result<?> hello() {
        return Result.ok();
    }

    @GetMapping("/user")
    public Result<List<UserDO>> searchUser(String queryString) {
        List<UserInfo> userInfos = userInfoMapper.selectList(Wrappers.query());
        List<Item> items = new ArrayList<>();
        for (var userInfo : userInfos) {
            int c1, c2;
            c1 = FuzzySearch.ratio(queryString, userInfo.getName());
            c2 = FuzzySearch.ratio(queryString, userInfo.getNickname());
            int maxC = Math.max(c1, c2);
            if (maxC >= 20) {
                items.add(new Item(userInfo, maxC));
            }
        }
        items.sort((a, b) -> b.c - a.c);
        List<UserDO> ret = new ArrayList<>();
        for (var item : items) {
            UserInfo userInfo = item.userInfo;
            UserState userState = userStateMapper.selectById(userInfo.getId());
            UserDO userDO = new UserDO(userInfo.getId(),
                    userInfo.getName(),
                    userInfo.getNickname(),
                    userInfo.getSignature(),
                    userInfo.getAuthority(),
                    userState.getRegisterTime(),
                    userState.getBannedUntil(),
                    userState.getBanCount()
            );
            ret.add(userDO);
        }
        return Result.ok(ret);
    }

    static record Item(UserInfo userInfo, int c) {}
}
