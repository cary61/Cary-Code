package top.cary61.carycode.judge.node.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.cary61.carycode.commons.entity.MicroTaskResult;
import top.cary61.carycode.commons.entity.Result;


@FeignClient("judge-server")
public interface JudgeServerRemote {
    @PostMapping("/accomplish")
    Result<?> accomplish(MicroTaskResult microTaskResult);

}
