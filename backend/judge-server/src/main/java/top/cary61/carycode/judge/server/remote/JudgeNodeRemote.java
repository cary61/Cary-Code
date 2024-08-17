package top.cary61.carycode.judge.server.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.cary61.carycode.commons.entity.MicroTask;
import top.cary61.carycode.commons.entity.Result;

@FeignClient("judge-node")
public interface JudgeNodeRemote {

    @PostMapping("/judge")
    Result judgeMicroTask(MicroTask microTask);
}
