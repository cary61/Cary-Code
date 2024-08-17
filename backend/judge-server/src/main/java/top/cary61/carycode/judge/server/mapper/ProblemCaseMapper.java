package top.cary61.carycode.judge.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.commons.entity.po.ProblemCase;

import java.util.List;

@Mapper
public interface ProblemCaseMapper extends BaseMapper<ProblemCase> {

    @Select("""
            Select * from `problem_case` where `problem_id` = #{problemId}
            order by `serial` ASC
            """)
    List<ProblemCase> getAllByProblemId(long problemId);
}
