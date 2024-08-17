package top.cary61.carycode.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.cary61.carycode.api.entity.enumeration.Difficulty;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;
import top.cary61.carycode.api.entity.po.Problem;

import java.util.List;

@Mapper
public interface ProblemMapper extends BaseMapper<Problem> {


    @Select("""
            <script>
                Select * from `problem`
                    <where>
                        <if test="difficulty != null">
                            and difficulty = #{difficulty}
                        </if>
                        
                        <if test="submissionState != null">
                            <choose>
                                <when test="submissionState.name() == 'UNATTEMPTED' ">
                                    and `id` not in
                                    (
                                        select `problem_id` from `submission_log`
                                            where `user_id` = #{userId}
                                    )
                                </when>
                                <when test="submissionState.name() == 'SOLVED' ">
                                    and `id` in
                                    (
                                        select `problem_id` from `submission_log`
                                            where
                                                `user_id` = #{userId}
                                                 and `judge_status` = 'AC'
                                    )
                                </when>
                                <when test="submissionState.name() == 'ATTEMPTED' ">
                                    and `id` in
                                    (
                                        select `problem_id` from `submission_log`
                                            where
                                                `user_id` = #{userId}
                                                 and `judge_status` != 'AC'
                                    )
                                    and `id` not in
                                    (
                                        select `problem_id` from `submission_log`
                                            where
                                                `user_id` = #{userId}
                                                 and `judge_status` = 'AC'
                                    )
                                </when>
                            </choose>
                        </if>
                        
                        <if test="tag != null">
                            and `id` in
                                (select `problem_id` from `problem_tag`
                                    where `name` = #{tag}
                                )
                        </if>
                    </where>
                limit #{pageSize}
                offset ${ (pageId - 1) * pageSize }
            </script>
            """)
    List<Problem> getList(int pageId,
                          int pageSize,
                          Difficulty difficulty,
                          SubmissionState submissionState,
                          String tag,
                          Long userId);


    @Select("""
            <script>
                Select count(*) from `problem`
                    <where>
                        <if test="difficulty != null">
                            and difficulty = #{difficulty}
                        </if>
                        
                        <if test="submissionState != null">
                            <choose>
                                <when test="submissionState.name() == 'UNATTEMPTED' ">
                                    and `id` not in
                                    (
                                        select `problem_id` from `submission_log`
                                            where `user_id` = #{userId}
                                    )
                                </when>
                                <when test="submissionState.name() == 'SOLVED' ">
                                    and `id` in
                                    (
                                        select `problem_id` from `submission_log`
                                            where
                                                `user_id` = #{userId}
                                                 and `judge_status` = 'AC'
                                    )
                                </when>
                                <when test="submissionState.name() == 'ATTEMPTED' ">
                                    and `id` in
                                    (
                                        select `problem_id` from `submission_log`
                                            where
                                                `user_id` = #{userId}
                                                 and `judge_status` != 'AC'
                                    )
                                    and `id` not in
                                    (
                                        select `problem_id` from `submission_log`
                                            where
                                                `user_id` = #{userId}
                                                 and `judge_status` = 'AC'
                                    )
                                </when>
                            </choose>
                        </if>
                        
                        <if test="tag != null">
                            and `id` in
                                (select `problem_id` from `problem_tag`
                                    where `name` = #{tag}
                                )
                        </if>
                    </where>
            </script>
            """)
    long getCount(Difficulty difficulty,
                  SubmissionState submissionState,
                  String tag,
                  Long userId);

    @Select("Select `title` from `problem` where `id` = #{id} ")
    String getTitle(long id);


}
