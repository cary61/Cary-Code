package top.cary61.carycode.commons.entity.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum JudgeStatus {

    AC(1),
    WA(2),
    RE(3),
    MLE(4),
    TLE(5),
    CE(6),
    SE(7),  // OJ 内部错误
    ;


    private int weight;

    private JudgeStatus(int weight) {
        this.weight = weight;
    }
}
