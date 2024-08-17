package top.cary61.carycode.node.function;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import top.cary61.carycode.commons.entity.MicroTask;
import top.cary61.carycode.commons.entity.MicroTaskResult;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;
import top.cary61.carycode.commons.entity.enums.Language;
import top.cary61.carycode.commons.entity.po.ProblemCase;
import top.cary61.carycode.judge.node.JudgeNodeApplication;
import top.cary61.carycode.judge.node.function.Judger;

import javax.annotation.Resource;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JudgeNodeApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JudgePythonTest {

    @Resource
    private Judger judger;

    private final int maxMemory = 134217728;
    private final int maxTimeMillis = 2000 ;

    @Test
    public void judgeACTest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(maxTimeMillis);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.PYTHON)
                .code("""
                        a, b = int(input()), int(input())
                        print(a + b)
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.AC, judgeStatus);
        System.out.println(microTaskResult);
    }

    @Test
    public void judgeWATest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(maxTimeMillis);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.PYTHON)
                .code("""
                        a, b = int(input()), int(input())
                        print(a - b)
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.WA, judgeStatus);
        System.out.println(microTaskResult);
    }

    @Test
    public void judgeRETest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(maxTimeMillis);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.PYTHON)
                .code("""
                        asdfasdf
                        asdfgsdfg
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.RE, judgeStatus);
        System.out.println(microTaskResult);
    }

    @Test
    public void judgeTLETest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(maxTimeMillis);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.PYTHON)
                .code("""
                        while True:
                            pass
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.TLE, judgeStatus);
        System.out.println(microTaskResult);
    }

    @Test
    public void judgeMLETest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(maxTimeMillis);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.PYTHON)
                .code("""
                        massive_dict = {}
                        for i in range(10**7):
                            massive_dict[i] = 'value' * 1000  # 为字典中的每个键存储一个大型字符串
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.MLE, judgeStatus);
        System.out.println(microTaskResult);
    }
}
