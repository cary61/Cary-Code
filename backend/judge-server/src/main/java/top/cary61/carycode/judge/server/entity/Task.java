package top.cary61.carycode.judge.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cary61.carycode.commons.entity.enums.Language;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Task {

    private long userId;

    private long problemId;

    private int casesAmount;

    private Language language;

    private String code;

    private LocalDateTime createTime;
}
