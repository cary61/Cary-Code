package top.cary61.carycode.judge.node.entity;

import top.cary61.carycode.commons.entity.enums.JudgeStatus;


public class JudgeException extends RuntimeException {

    private final JudgeStatus judgeStatus;

    public JudgeException(JudgeStatus judgeStatus) {
        this.judgeStatus = judgeStatus;
    }

    public JudgeException(JudgeStatus judgeStatus, String msg) {
        super(msg);
        this.judgeStatus = judgeStatus;
    }

    public JudgeStatus getJudgeStatus() {
        return judgeStatus;
    }
}
