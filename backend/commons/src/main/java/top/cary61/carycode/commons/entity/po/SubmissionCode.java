package top.cary61.carycode.commons.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.Language;

@Data
@TableName("submission_code")
@AllArgsConstructor
public class SubmissionCode {

    private Long id;

    private String submissionUuid;

    private Language language;

    private String code;
}
