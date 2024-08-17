package top.cary61.carycode.api.service;

import org.springframework.stereotype.Service;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;
import top.cary61.carycode.api.mapper.SubmissionLogMapper;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;
import top.cary61.carycode.commons.entity.po.SubmissionLog;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SubmissionService {

    @Resource
    private SubmissionLogMapper submissionLogMapper;

    public SubmissionState getState(long userId, long problemId) {
        List<SubmissionLog> submissionLogs = submissionLogMapper.getListByUserIdAndProblemId(userId, problemId);
        if (submissionLogs.isEmpty()) {
            return SubmissionState.UNATTEMPTED;
        }
        for (SubmissionLog submissionLog : submissionLogs) {
            if (submissionLog.getJudgeStatus() == JudgeStatus.AC) {
                return SubmissionState.SOLVED;
            }
        }
        return SubmissionState.ATTEMPTED;
    }
}
