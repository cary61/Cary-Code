package top.cary61.carycode.api.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.api.entity.enumeration.Authority;

@Data
@AllArgsConstructor
@TableName("user_info")
public class UserInfo {

    private Long id;

    private String name;

    private String nickname;

    private String avatar;

    private String signature;

    private Authority authority;
}
