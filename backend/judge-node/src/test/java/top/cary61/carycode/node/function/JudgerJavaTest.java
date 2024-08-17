package top.cary61.carycode.node.function;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import top.cary61.carycode.commons.entity.MicroTaskResult;
import top.cary61.carycode.commons.entity.MicroTask;
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
public class JudgerJavaTest {

    @Resource
    private Judger judger;

    private final int maxMemory = 134217728;

    @Test
    public void judgeACTest() {
        ProblemCase problemCase = new ProblemCase();
        problemCase.setProblemId(1L);
        problemCase.setSerial(1);
        problemCase.setInput("1\n1");
        problemCase.setOutput("2");
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(2000);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.JAVA)
                .code("""
                        import java.util.Scanner;
                        
                        public class Main {
                            public static void main(String[] args) {
                                Scanner sc = new Scanner(System.in);
                                int a = sc.nextInt(), b = sc.nextInt();
                                System.out.println(a + b);
                            }
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

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.JAVA)
                .code("""
                        import java.util.Scanner;
                        
                        public class Main {
                            public static void main(String[] args) {
                                Scanner sc = new Scanner(System.in);
                                int a = sc.nextInt(), b = sc.nextInt();
                                System.out.println(a - b);
                            }
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

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.JAVA)
                .code("""
                        iasdfasdf
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

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.JAVA)
                .code("""
                        import java.util.Scanner;
                        
                        public class Main {
                            public static void main(String[] args) {
                                Scanner sc = new Scanner(System.in);
                                int a = sc.nextInt(), b = sc.nextInt();
                                System.out.println(a - b);
                                throw new RuntimeException("111");
                            }
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

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.JAVA)
                .code("""
                        import java.util.Scanner;
                        
                        public class Main {
                            public static void main(String[] args) {
                                while (true) {
                                
                                }
                            }
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
        problemCase.setMaxMemory(maxMemory);
        problemCase.setMaxTimeMillis(2000);

        MicroTask microTask = MicroTask.builder()
                .uuid(UUID.randomUUID().toString())
                .language(Language.JAVA)
                .code("""
                        import java.util.Scanner;
                        
                        public class Main {
                            public static void main(String[] args) {
                            Scanner sc = new Scanner(System.in);
                                int a = sc.nextInt(), b = sc.nextInt();
                                System.out.println(a + b);
                                
                                int[] arr = new int[33554432];
                            }
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
