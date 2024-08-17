package top.cary61.carycode.api.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@TableName("invalid_token")
public class InvalidToken {

    private Long id;

    private String uuid;

    private LocalDateTime expiredTime;
}
