package top.cary61.carycode.commons.entity;

import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;

@Data
@Builder
public class MicroTaskResult {

    private String uuid;

    private Integer caseSerial;

    private JudgeStatus judgeStatus;

    private Integer timeMillis;

    private Integer memory;
}
