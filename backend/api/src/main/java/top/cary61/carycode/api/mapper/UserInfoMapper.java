package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import top.cary61.carycode.api.entity.po.UserInfo;


@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("Select * from `user_info` where `name` = #{name} ")
    UserInfo selectByName(String name);
}
