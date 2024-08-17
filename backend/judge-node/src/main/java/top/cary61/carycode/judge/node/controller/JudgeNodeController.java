package top.cary61.carycode.judge.node.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.cary61.carycode.commons.entity.MicroTask;
import top.cary61.carycode.commons.entity.Result;
import top.cary61.carycode.judge.node.function.MicroTaskQueue;

import javax.annotation.Resource;

@RestController
public class JudgeNodeController {

    @Resource
    private MicroTaskQueue microTaskQueue;

    @PostMapping("/judge")
    public Result<?> judgeMicroTask(@RequestBody MicroTask microTask) {
        microTaskQueue.push(microTask);
        return Result.ok();
    }

    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }
}
