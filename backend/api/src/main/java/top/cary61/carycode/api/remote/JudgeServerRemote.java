package top.cary61.carycode.api.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import top.cary61.carycode.commons.entity.JudgeRequest;
import top.cary61.carycode.commons.entity.PollingMessage;
import top.cary61.carycode.commons.entity.Result;
import top.cary61.carycode.commons.entity.po.SubmissionLog;

@FeignClient("judge-server")
public interface JudgeServerRemote {

    @PostMapping("/push")
    Result<PollingMessage<SubmissionLog>> pushJudgeTask(JudgeRequest judgeRequest);

    // 得到预计的剩余时间
    @GetMapping("/time")
    Result<PollingMessage<SubmissionLog>> getRestTime(@RequestParam String uuid);
}
