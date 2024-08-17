package top.cary61.carycode.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cary61.carycode.commons.entity.Result;

import java.time.LocalDateTime;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Result<?> hello() {
        return Result.ok("Hello, world! 现在的时间：" + LocalDateTime.now());
    }


    @PostMapping("/hello")
    public Result<String> postHello() {
        return Result.ok("POST请求成功");
    }

    @GetMapping("/bad")
    public Result<?> bad() {
        return Result.fail("一条错误信息！");
    }

}


