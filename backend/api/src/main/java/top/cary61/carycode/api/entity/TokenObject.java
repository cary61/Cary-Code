package top.cary61.carycode.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cary61.carycode.api.entity.po.UserState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TokenObject {

    private long userId;

    private String uuid;

    private LocalDateTime issuedTime;

    private LocalDateTime expiredTime;

    // 解析 Token 时会从数据库获得一次
    private UserState userState;
}
