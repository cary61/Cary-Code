package top.cary61.carycode.api.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.commons.entity.po.SubmissionCode;

@Mapper
public interface SubmissionCodeMapper extends BaseMapper<SubmissionCode> {

    @Select("Select * from `submission_code` where `submission_uuid` = #{uuid} ")
    SubmissionCode getByUuid(String uuid);
}
