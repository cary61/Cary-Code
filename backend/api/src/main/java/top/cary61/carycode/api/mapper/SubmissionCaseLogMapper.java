package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.commons.entity.po.SubmissionCaseLog;

import java.util.List;

@Mapper
public interface SubmissionCaseLogMapper extends BaseMapper<SubmissionCaseLog> {

    @Select("Select * from `submission_case_log` where `submission_uuid` = #{uuid} ")
    List<SubmissionCaseLog> getListByUuid(String uuid);
}
