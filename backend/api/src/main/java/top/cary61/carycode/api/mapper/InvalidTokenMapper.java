package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.api.entity.po.InvalidToken;
import top.cary61.carycode.api.entity.po.UserInfo;

@Mapper
public interface InvalidTokenMapper extends BaseMapper<InvalidToken> {

    @Select("Select * from `invalid_token` where `uuid` = #{uuid} ")
    UserInfo selectByUuid(String uuid);
}
