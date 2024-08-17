package top.cary61.carycode.commons.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@TableName("submission_log")
public class SubmissionLog {

    private Long id;

    // 本次提交的唯一序列号
    private String uuid;

    private Long userId;

    private Long problemId;

    private JudgeStatus judgeStatus;

    private Integer timeMillis;

    private Integer memory;

    private LocalDateTime createTime;
}
