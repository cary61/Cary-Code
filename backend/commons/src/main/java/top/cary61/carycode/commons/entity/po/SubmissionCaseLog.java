package top.cary61.carycode.commons.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;

import java.util.UUID;

@Data
@Builder
@TableName("submission_case_log")
public class SubmissionCaseLog {

    private Long id;

    private String submissionUuid;

    private Integer caseSerial;

    private JudgeStatus judgeStatus;

    private Integer timeMillis;

    private Integer memory;
}
