package top.cary61.carycode.api.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.cary61.carycode.api.entity.enumeration.Difficulty;
import top.cary61.carycode.api.entity.enumeration.ProblemState;

@Data
@TableName("problem")
public class Problem {

    private Long id;

    private String number;  // 编号

    private String title;

    private Long uploaderUserId;  // 题目上传者id

    private String background;

    private String description;

    private String inputFormat;

    private String outputFormat;

    private String dataRange;

    private Difficulty difficulty;

    private ProblemState state;
}
