package top.cary61.carycode.commons.entity;

import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.Language;

@Data
@Builder
public class JudgeRequest {

    private Long userId;

    private Long problemId;

    private Language language;

    private String code;
}
