package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.api.entity.po.UserState;

@Mapper
public interface UserStateMapper extends BaseMapper<UserState> {

    @Select("Select * from `user_state` where `name` = #{name} ")
    UserState selectByName(String name);
}
