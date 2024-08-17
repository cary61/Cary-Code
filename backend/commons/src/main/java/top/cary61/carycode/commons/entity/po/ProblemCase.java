package top.cary61.carycode.commons.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("problem_case")
public class ProblemCase {

    private Long id;

    private Long problemId;

    private Integer serial;

    private String input;

    private String output;

    private Integer maxTimeMillis;

    private Integer maxMemory;

    private LocalDateTime createTime;
}
