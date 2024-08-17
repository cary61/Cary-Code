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
public class JudgeCppTest {

    @Resource
    private Judger judger;

    private final int maxMemory = 33554432;

    @Test
    public void judgeACTest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(2000);

        String uuid = UUID.randomUUID().toString();
        System.out.println("当前UUID: " + uuid);
        MicroTask microTask = MicroTask.builder()
                .uuid(uuid)
                .language(Language.CPP)
                .code("""
                        #include<iostream>
                        using namespace std;
                        
                        int main() {
                            int a, b;
                            cin >> a >> b;
                            cout << (a + b);
                            
                            return 0;
                        }
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
        problemCase.setMaxTimeMillis(2000);

        String uuid = UUID.randomUUID().toString();
        System.out.println("当前UUID: " + uuid);
        MicroTask microTask = MicroTask.builder()
                .uuid(uuid)
                .language(Language.CPP)
                .code("""
                        #include<iostream>
                        using namespace std;
                        
                        int main() {
                            int a, b;
                            cin >> a >> b;
                            cout << (a - b);
                            
                            return 0;
                        }
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.WA, judgeStatus);
        System.out.println(microTaskResult);
    }

    @Test
    public void judgeCETest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(2000);

        String uuid = UUID.randomUUID().toString();
        System.out.println("当前UUID: " + uuid);
        MicroTask microTask = MicroTask.builder()
                .uuid(uuid)
                .language(Language.CPP)
                .code("""
                        asdfasdf
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.CE, judgeStatus);
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
        problemCase.setMaxTimeMillis(2000);

        String uuid = UUID.randomUUID().toString();
        System.out.println("当前UUID: " + uuid);
        MicroTask microTask = MicroTask.builder()
                .uuid(uuid)
                .language(Language.CPP)
                .code("""
                        #include<iostream>
                        using namespace std;
                        
                        int main() {
                            int a, b;
                            cin >> a >> b;
                            cout << (a + b);
                            
                            return -1;
                        }
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
        problemCase.setMaxTimeMillis(2000);

        String uuid = UUID.randomUUID().toString();
        System.out.println("当前UUID: " + uuid);
        MicroTask microTask = MicroTask.builder()
                .uuid(uuid)
                .language(Language.CPP)
                .code("""
                        #include<iostream>
                        using namespace std;
                        
                        int main() {
                            while (true) {
                            
                            }
                            
                            return -1;
                        }
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
        problemCase.setMaxMemory(maxMemory / 2);
        problemCase.setMaxTimeMillis(2000);

        String uuid = UUID.randomUUID().toString();
        System.out.println("当前UUID: " + uuid);
        MicroTask microTask = MicroTask.builder()
                .uuid(uuid)
                .language(Language.CPP)
                .code("""
                        #include<iostream>
                        using namespace std;
                        
                        int main() {
                            
                            int* arr = new int[3355443200];
                            arr[0] = 123;
                            arr[123] = 12345;
                            arr[12345] = arr[1] + arr[1234];
                            for (int i = 0; i < 100000; i++) {
                                arr[i + i / 2] = i % 3;
                            }
                            
                            return 0;
                        }
                        """)
                .problemCase(problemCase)
                .build();
        MicroTaskResult microTaskResult = judger.judge(microTask);
        JudgeStatus judgeStatus = microTaskResult.getJudgeStatus();
        Assertions.assertEquals(JudgeStatus.MLE, judgeStatus);
        System.out.println(microTaskResult);
    }
}
