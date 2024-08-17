package top.cary61.carycode.api.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@TableName("user_state")
public class UserState {

    private Long id;

    private String name;

    private String passwordHash;

    private LocalDateTime passwordUpdateTime;

    private LocalDateTime registerTime;

    private LocalDateTime bannedUntil;

    private Integer banCount;
}
