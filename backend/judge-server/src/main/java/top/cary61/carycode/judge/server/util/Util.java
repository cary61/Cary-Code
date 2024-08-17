package top.cary61.carycode.judge.server.util;

import top.cary61.carycode.commons.entity.enums.JudgeStatus;
import top.cary61.carycode.commons.entity.po.SubmissionCaseLog;
import top.cary61.carycode.commons.entity.po.SubmissionLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Util {

    /**
     *
     * @param submissionCaseLogs 调用方自行确认所有 case logs 来自同一 submission，并且不是空数组
     * @return 合并后总的 submission
     */
    public static SubmissionLog merge(List<SubmissionCaseLog> submissionCaseLogs,
                                      long problemId,
                                      long userId,
                                      LocalDateTime time) {
        // merge
        Integer totalTimeMillis = 0, totalMemory = 0;
        JudgeStatus mergedStatus = null;
        for (SubmissionCaseLog submissionCaseLog : submissionCaseLogs) {
            if (submissionCaseLog.getTimeMillis() != null) {
                totalTimeMillis += submissionCaseLog.getTimeMillis();
            }
            if (submissionCaseLog.getMemory() != null) {
                totalMemory += submissionCaseLog.getMemory();
            }
            JudgeStatus judgeStatus = submissionCaseLog.getJudgeStatus();
            // 仅在第一次赋AC，之后未再赋其他值，才是AC
            if (mergedStatus == null) {
                mergedStatus = judgeStatus;
            } else if (judgeStatus.getWeight() > mergedStatus.getWeight()) {
                mergedStatus = judgeStatus;
            }
        }
        if (mergedStatus != JudgeStatus.AC) {
            totalTimeMillis = null;
            totalMemory = null;
        }
        // build
        SubmissionCaseLog oneCaseLog = submissionCaseLogs.get(0);
        return SubmissionLog.builder()
                .uuid(oneCaseLog.getSubmissionUuid())
                .userId(userId)
                .problemId(problemId)
                .judgeStatus(mergedStatus)
                .timeMillis(totalTimeMillis)
                .memory(totalMemory)
                .createTime(time)
                .build();
    }
}
