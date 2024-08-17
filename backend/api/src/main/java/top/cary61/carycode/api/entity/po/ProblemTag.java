package top.cary61.carycode.api.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("problem_tag")
public class ProblemTag {

    private Long id;

    private Long problemId;

    private String name;  // 标签名
}
