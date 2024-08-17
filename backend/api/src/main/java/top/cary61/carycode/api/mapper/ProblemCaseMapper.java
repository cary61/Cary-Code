package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.commons.entity.po.ProblemCase;

import java.util.List;

@Mapper
public interface ProblemCaseMapper extends BaseMapper<ProblemCase> {

    @Select("Select * from `problem_case` where `problem_id` = #{problemId} ")
    List<ProblemCase> getListByProblemId(long problemId);

    @Delete("Delete from `problem_case` where `problem_id` = #{problemId} ")
    int deleteBatchByProblemId(long problemId);
}
