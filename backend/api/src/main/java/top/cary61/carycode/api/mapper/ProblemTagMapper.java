package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.api.entity.po.ProblemTag;

import java.util.List;

@Mapper
public interface ProblemTagMapper extends BaseMapper<ProblemTag> {

    @Select("Select * from `problem_tag` where `problem_id` = #{problemId} ")
    List<ProblemTag> getAll(long problemId);
}
