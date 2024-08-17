package top.cary61.carycode.judge.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.cary61.carycode.commons.entity.MicroTaskResult;
import top.cary61.carycode.commons.entity.JudgeRequest;
import top.cary61.carycode.commons.entity.PollingMessage;
import top.cary61.carycode.commons.entity.Result;
import top.cary61.carycode.commons.entity.po.SubmissionLog;
import top.cary61.carycode.judge.server.function.MessageQueue;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
public class JudgeServerController {

    @Resource
    private MessageQueue messageQueue;

    // 向队列添加一条评测
    @PostMapping("/push")
    public Result<PollingMessage<SubmissionLog>> pushJudgeTask(@RequestBody JudgeRequest judgeRequest) {
        System.out.println(judgeRequest);
        return Result.ok(messageQueue.push(judgeRequest));
    }

    // 回馈评测的其中一个任务
    @PostMapping("/accomplish")
    public Result<?> accomplish(@RequestBody MicroTaskResult microTaskResult) {
        messageQueue.accomplish(microTaskResult.getUuid(),
                                microTaskResult.getCaseSerial(),
                                microTaskResult.getJudgeStatus(),
                                microTaskResult.getTimeMillis(),
                                microTaskResult.getMemory()
        );
        return Result.ok();
    }

    // 得到预计的剩余时间
    @GetMapping("/time")
    public Result<PollingMessage<SubmissionLog>> getRestTime(String uuid) {
        PollingMessage<SubmissionLog> pollingMessage = messageQueue.getRestTime(uuid);
        if (pollingMessage == null) {
            return Result.fail("无此任务");
        }
        return Result.ok(pollingMessage);
    }
}
