package top.cary61.carycode.judge.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("top.cary61.carycode.judge.server.mapper")
@EnableFeignClients
public class JudgeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JudgeServerApplication.class, args);
    }
}
