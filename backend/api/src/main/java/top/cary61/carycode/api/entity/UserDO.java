package top.cary61.carycode.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cary61.carycode.api.entity.enumeration.Authority;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDO {
    private Long id;

    private String name;

    private String nickname;

    private String signature;

    private Authority authority;

    private LocalDateTime registerTime;

    private LocalDateTime bannedUntil;

    private Integer banCount;
}
