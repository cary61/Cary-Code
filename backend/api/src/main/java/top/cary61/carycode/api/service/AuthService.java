package top.cary61.carycode.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import top.cary61.carycode.api.entity.enumeration.Authority;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.api.entity.po.UserState;
import top.cary61.carycode.api.mapper.UserInfoMapper;
import top.cary61.carycode.api.mapper.UserStateMapper;
import top.cary61.carycode.api.util.Hash;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AuthService {

    @Value("${user.default.avatar}")
    private String defaultAvatar;

    @Value("${user.default.signature}")
    private String defaultSignature;

    @Resource
    UserInfoMapper userInfoMapper;
    @Resource
    UserStateMapper userStateMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean register(String username, String password) {
        String passwordHash = Hash.hash(password);
        LocalDateTime now = LocalDateTime.now();
        int count1 = userInfoMapper.insert(
                new UserInfo(null, username, username, defaultAvatar, defaultSignature, Authority.USER)
        );
        int count2 = userStateMapper.insert(
                new UserState(null, username, passwordHash, now, now,null,0)
        );
        if (count1 == 1 && count2 == 1) {
            return true;
        }
        throw new RuntimeException();
    }

    public boolean checkPassword(String username, String password) {
        UserState userState = userStateMapper.selectByName(username);
        return Objects.equals(
                userState.getPasswordHash(),
                Hash.hash(password)
        );
    }
}
