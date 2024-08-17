package top.cary61.carycode.api.controller;

import org.springframework.web.bind.annotation.*;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.exception.ServiceException;
import top.cary61.carycode.api.mapper.ProblemCaseMapper;
import top.cary61.carycode.api.remote.JudgeServerRemote;
import top.cary61.carycode.commons.entity.PollingMessage;
import top.cary61.carycode.commons.entity.po.SubmissionLog;
import top.cary61.carycode.api.mapper.SubmissionLogMapper;
import top.cary61.carycode.commons.entity.JudgeRequest;
import top.cary61.carycode.commons.entity.Result;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/judge")
public class JudgeController {

    @Resource
    private SubmissionLogMapper submissionLogMapper;

    @Resource
    private JudgeServerRemote judgeServerRemote;

    @Resource
    private Context context;

    @PostMapping
    public Result<PollingMessage<SubmissionLog>> judge(@RequestBody JudgeRequest judgeRequest) {
        judgeRequest.setUserId(context.getUserId());
        return judgeServerRemote.pushJudgeTask(judgeRequest);
    }

    @GetMapping("/result")
    public Result<PollingMessage<SubmissionLog>> getResult(String uuid) {
        Result<PollingMessage<SubmissionLog>> result = judgeServerRemote.getRestTime(uuid);

        // result 为 true，任务正在运行，直接返回评测服务器返回的期望时间
        if (result.isOk()) {
            return result;
        }

        // result 为 false，评测机未找到此任务
        SubmissionLog submissionLog = submissionLogMapper.getByUuid(uuid);
        if (submissionLog == null) {
            throw new ServiceException("评测结果不存在！");
        }
        return Result.ok(new PollingMessage<>(uuid, true, -1, submissionLog));
    }
}
