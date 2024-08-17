package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.commons.entity.po.SubmissionLog;

import java.util.List;

@Mapper
public interface SubmissionLogMapper extends BaseMapper<SubmissionLog> {

    @Select("Select * from `submission_log` where `uuid` = #{uuid} ")
    SubmissionLog getByUuid(String uuid);

    @Select("Select * from `submission_log` where `user_id` = #{userId} and `problem_id` = #{problemId} ")
    List<SubmissionLog> getListByUserIdAndProblemId(long userId, long problemId);
}
