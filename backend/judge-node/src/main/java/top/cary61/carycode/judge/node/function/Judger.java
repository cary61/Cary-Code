package top.cary61.carycode.judge.node.function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.cary61.carycode.commons.entity.MicroTaskResult;
import top.cary61.carycode.commons.entity.MicroTask;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;
import top.cary61.carycode.commons.entity.enums.Language;
import top.cary61.carycode.commons.entity.po.ProblemCase;
import top.cary61.carycode.judge.node.entity.JudgeException;
import top.cary61.carycode.judge.node.entity.JudgeResourceInfo;
import top.cary61.carycode.judge.node.util.Command;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Component
public class Judger {

    @Resource
    private Command command;
    @Resource
    private DockerScanner dockerScanner;

    private final ThreadLocal<MicroTask> microTaskLocal = new ThreadLocal<>();
    private final ThreadLocal<String> containerName = new ThreadLocal<>();

    public MicroTaskResult judge(MicroTask microTask) {
        microTaskLocal.set(microTask);
        ProblemCase problemCase = microTask.getProblemCase();
        containerName.set(microTask.getUuid() + "-" + problemCase.getSerial());
        int timeMultiplier = 1, memoryMultiplier = 1;
        if (microTask.getLanguage() == Language.JAVA) {
            timeMultiplier = 2;
            memoryMultiplier = 2;
        } else if (microTask.getLanguage() == Language.PYTHON) {
            timeMultiplier = 10;
            memoryMultiplier = 2;
        }
        int maxTimeMillis = microTask.getProblemCase().getMaxTimeMillis();
        microTask.getProblemCase().setMaxTimeMillis(maxTimeMillis * timeMultiplier);
        int maxMemory = microTask.getProblemCase().getMaxMemory();
        microTask.getProblemCase().setMaxMemory(maxMemory * memoryMultiplier);
        
        MicroTaskResult microTaskResult = MicroTaskResult.builder()
                .uuid(microTask.getUuid())
                .caseSerial(problemCase.getSerial())
                .build();
        JudgeResourceInfo judgeResourceInfo = null;
        try {
            judgeResourceInfo = runJudge();
        } catch (JudgeException e) {  // 判题问题
            microTaskResult.setJudgeStatus(e.getJudgeStatus());
            if (e.getJudgeStatus() == JudgeStatus.SE){
                log.error(e.getMessage() + "@" + containerName.get());
            }
        } catch (Exception e) { // OJ 内部错误
            microTaskResult.setJudgeStatus(JudgeStatus.SE);
            log.error(e.getMessage() + "@" + containerName.get());
            throw new RuntimeException(e);
        } finally {
            microTaskLocal.remove();
            containerName.remove();
        }

        if (microTaskResult.getJudgeStatus() == null) {
            microTaskResult.setJudgeStatus(JudgeStatus.AC);
            if (judgeResourceInfo != null) {
                microTaskResult.setTimeMillis(judgeResourceInfo.getTimeMillis());
                microTaskResult.setMemory(judgeResourceInfo.getMemory());
            }
        }
        return microTaskResult;
    }

    private JudgeResourceInfo runJudge() {
        MicroTask microTask = microTaskLocal.get();
        ProblemCase problemCase = microTask.getProblemCase();
        JudgeResourceInfo judgeResourceInfo;

        Process process = null;
        try {
            // 创建代码文件
            createFile(microTask.getCode(), command.localCodeFilePath(containerName.get(), microTask.getLanguage()));
            // 启动 Docker 容器
            process = startContainer();
            dockerScanner.startScan(containerName.get());
            // 传递用户代码到容器中
            sendCodeFileToContainer();
            // 编译用户程序
            compileUserProgram();
            // 创建 input.txt
            createFile(problemCase.getInput(), command.localInputFilePath(containerName.get()));
            // 传递input.txt到容器中
            sendInputFileToContainer();
            // 执行用户程序
            judgeResourceInfo = executeUserProgram();
            // 从容器中取回output.txt
            retrieveOutputFile();
            // 检查 output.txt 文件
            checkOutputFile();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 结束持有容器的进程
            if (process != null) {
                process.destroyForcibly();
            }
            if (!command.isKeepTrace()) {
                cleanTempFiles();
            }
            // 停止监听线程
            dockerScanner.getMemoryUsed();
        }
        return judgeResourceInfo;
    }

    private Process startContainer() throws IOException {
        Language language = microTaskLocal.get().getLanguage();

        ProcessBuilder pb = new ProcessBuilder(
                command.run(containerName.get(), language).asList()
        );
        pb.directory(new File("/"));
        Process process = pb.start();
        waitForContainerInit();
        return process;
    }

    private void waitForContainerInit() throws IOException {
        while (true) {
            ProcessBuilder pb = new ProcessBuilder(
                    command.ps(containerName.get()).asList()
            );
            Process process = pb.start();
            try (BufferedReader reader = Optional.of(process.getInputStream())
                         .map(InputStreamReader::new)
                         .map(BufferedReader::new)
                         .get();
                 BufferedReader errorReader = Optional.of(process.getErrorStream())
                         .map(InputStreamReader::new)
                         .map(BufferedReader::new)
                         .get()) {
                if (errorReader.readLine() != null) {  // 连接不到 Docker Server
                    throw new JudgeException(JudgeStatus.SE, "连接Docker Sever失败");
                }
                if (Objects.equals(reader.readLine(), containerName.get())) {
                    return;
                }
            } finally {
                process.destroyForcibly();
            }
        }
    }

    private void sendCodeFileToContainer() throws IOException, InterruptedException {
        ProcessBuilder cpBuilder = new ProcessBuilder(
                command.sendCodeFileToContainer(containerName.get(), microTaskLocal.get().getLanguage()).asList()
        );

        Process cpProcess = null;
        try {
            cpProcess = cpBuilder.start();
            int cpExitCode = cpProcess.waitFor();
            if (cpExitCode != 0) {
                throw new JudgeException(JudgeStatus.SE, "移动代码文件到容器失败");
            }
        } finally {
            if (cpProcess != null) {
                cpProcess.destroyForcibly();
            }
        }
    }

    private void sendInputFileToContainer() throws IOException, InterruptedException {
        ProcessBuilder cpBuilder = new ProcessBuilder(
                command.sendInputFileToContainer(containerName.get()).asList()
        );

        Process cpProcess = null;
        try {
            cpProcess = cpBuilder.start();
            int cpExitCode = cpProcess.waitFor();
            if (cpExitCode != 0) {
                throw new JudgeException(JudgeStatus.SE, "移动input.txt文件到容器失败");
            }
        } finally {
            if (cpProcess != null) {
                cpProcess.destroyForcibly();
            }
        }
    }


    private void compileUserProgram() throws IOException, InterruptedException {
        Language language = microTaskLocal.get().getLanguage();
        if (language == Language.PYTHON) {
            return;  // 不需要编译
        }

        ProcessBuilder compileBuilder = new ProcessBuilder(
                command.compile(containerName.get(), language).asList()
        );
        Process compileProcess = null;
        try {
            compileProcess = compileBuilder.start();
            boolean exited = compileProcess.waitFor(5000L, TimeUnit.MILLISECONDS);
            if (!exited) {
                throw new JudgeException(JudgeStatus.CE);
            }
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                throw new JudgeException(JudgeStatus.CE);
            }
        } finally {
            if (compileProcess != null) {
                compileProcess.destroyForcibly();
            }
        }
    }

    private JudgeResourceInfo executeUserProgram() throws IOException, InterruptedException {
        MicroTask microTask = microTaskLocal.get();
        Language language = microTask.getLanguage();
        int maxTimeMillis = microTask.getProblemCase().getMaxTimeMillis();
        int maxMemory = microTask.getProblemCase().getMaxMemory();

        ProcessBuilder executeBuilder = new ProcessBuilder(
                command.judge(containerName.get(), language, maxTimeMillis, maxMemory).asList()
        );

        Process executeProcess = null;
        try {
            long startTime = System.nanoTime();
            executeProcess = executeBuilder.start();
            int executeExitCode = executeProcess.waitFor();
//            boolean onTime = executeProcess.waitFor(microTask.getProblemCase().getMaxTimeMillis(), TimeUnit.MILLISECONDS);
            long endTime = System.nanoTime();
            long timeMillis = (endTime - startTime) / 1_000_000;

            System.out.println("execute code: " + executeExitCode);

            long memory = Math.max(dockerScanner.getMemoryUsed(), getCurrentMemoryUsed());
            if (memory > maxMemory) {
                throw new JudgeException(JudgeStatus.MLE);
            }

            if (executeExitCode != 0) {
                if (executeExitCode == 139 || executeExitCode == 134 /* 容器 超内存退出 */ ) {
                    throw new JudgeException(JudgeStatus.MLE);
                }
                if (executeExitCode == 137 /* Linux rlimit 超时退出 */ || timeMillis > maxTimeMillis) {
                    throw new JudgeException(JudgeStatus.TLE);
                }
                try (BufferedReader reader = Optional.of(executeProcess.getErrorStream())
                        .map(InputStreamReader::new)
                        .map(BufferedReader::new)
                        .get()) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("java.lang.OutOfMemoryError")) {
                            throw new JudgeException(JudgeStatus.MLE);
                        }
                    }
                }
                throw new JudgeException(JudgeStatus.RE);
            }
            return new JudgeResourceInfo((int)timeMillis, (int)memory);

        } finally {
            if (executeProcess != null) {
                executeProcess.destroyForcibly();
            }
        }

    }

    private void retrieveOutputFile() throws IOException, InterruptedException {
        ProcessBuilder cpBuilder = new ProcessBuilder(
                command.retrieveOutputFile(containerName.get()).asList()
        );

        Process cpProcess = null;
        try {
            cpProcess = cpBuilder.start();
            int cpExitCode = cpProcess.waitFor();
            cpProcess.destroyForcibly();
            if (cpExitCode != 0) {
                throw new JudgeException(JudgeStatus.SE, "从容器取出output.txt文件失败");
            }
        } finally {
            if (cpProcess != null) {
                cpProcess.destroyForcibly();
            }
        }
    }

    private long getCurrentMemoryUsed() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                command.statsFor(containerName.get()).asList()
        );

        Process process = null;
        try {
            process = pb.start();
            try (BufferedReader reader = Optional.of(process.getInputStream())
                    .map(InputStreamReader::new)
                    .map(BufferedReader::new)
                    .get()) {
                reader.readLine();
                long ret = DockerScanner.parseMemory(reader.readLine());
                process.destroyForcibly();
                return ret;
            }
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    private void checkOutputFile() throws IOException {
        MicroTask microTask = microTaskLocal.get();
        ProblemCase problemCase = microTask.getProblemCase();
        String output = Files.readString(Path.of(command.localOutputFilePath(containerName.get()))).trim();
        String answer = problemCase.getOutput().trim();
        String[] outputLines = output.split("\n");
        String[] answerLines = answer.split("\n");
        int n = outputLines.length;
        if (answerLines.length != n) {
            throw new JudgeException(JudgeStatus.WA);
        }
        for (int i = 0; i < n; i++) {
            if (!Objects.equals(outputLines[i], answerLines[i])) {
                throw new JudgeException(JudgeStatus.WA);
            }
        }
    }

    private void createFile(String text, String destPath) throws IOException {
        File dir = new File(destPath).getParentFile();
        boolean b = dir.mkdirs();
        if (!b && !dir.exists()) {
            throw new JudgeException(JudgeStatus.SE, "创建文件失败: " + destPath);
        }
        Files.writeString(Path.of(destPath), text);
    }


    private void cleanTempFiles() {
        Path folderPath = Path.of(command.localTempDir(containerName.get()));
        try (Stream<Path> paths = Files.walk(folderPath)) {
            paths.sorted((p1, p2) -> -p1.compareTo(p2)) // 从深到浅遍历文件夹
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    log.error("删除文件失败: " + path);
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            log.error("删除文件夹失败: " + folderPath);
            e.printStackTrace();
        }
    }
}
