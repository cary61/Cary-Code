package top.cary61.carycode.commons.entity;

import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.Language;
import top.cary61.carycode.commons.entity.po.ProblemCase;

import java.util.UUID;

@Data
@Builder
public class MicroTask {

    private String uuid;

    private ProblemCase problemCase;

    private Language language;

    private String code;
}
