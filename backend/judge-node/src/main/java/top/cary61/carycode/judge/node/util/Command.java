package top.cary61.carycode.judge.node.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary61.carycode.commons.entity.Delimiter;
import top.cary61.carycode.commons.entity.enums.Language;
import top.cary61.carycode.commons.util.Sequence;


@Component
public class Command {


    @Value("${judge.local-temp-base-dir}")
    private String baseDir;
    @Value("${judge.container-base-dir}")
    private String containerDir;

    @Value("${judge.keep-trace}")
    private boolean keepTrace;


    @Value("${judge.images.cpp}")
    private String cppImage;
    @Value("${judge.images.java}")
    private String javaImage;
    @Value("${judge.images.python}")
    private String pythonImage;

    private String image(Language language) {
        return switch (language) {
            case CPP -> cppImage;
            case JAVA -> javaImage;
            case PYTHON -> pythonImage;
            default -> throw new IllegalArgumentException(language.name());
        };
    }


    public Sequence run(String containerName, Language language) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "run", "-i")
                .$("-m", "500M")
                .$("--memory-swap", "512M")
                .$("--name", containerName)
                .when(!keepTrace).$("--rm")
                .end()
                .$(image(language));
    }

    public Sequence ps(String containerName) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "ps")
                .$("--filter", "name=" + containerName, "--format", "{{.Names}}");
    }

    private Sequence execInSh(String containerName, String command) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "exec")
                .$("-w", containerDir, containerName)
                .$("sh", "-c", "\"" + command + "\"");
    }

    public Sequence judge(String containerName, Language language, int maxTimeMillis, int maxMemory) {
        return execInSh(
                containerName,
                judgeCommand(language, maxTimeMillis, maxMemory).toString()
        );
    }

    public Sequence statsFor(String containerName) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "stats")
                .$("--no-stream", containerName);
    }

    public Sequence compile(String containerName, Language language) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "exec")
                .$("-w", containerDir, containerName)
                .$(compileCommand(language));
    }

    private Sequence cpToContainer(String containerName, String hostPath, String containerPath) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "cp")
                .$(hostPath, containerName + ":" + containerPath);
    }

    public Sequence sendCodeFileToContainer(String containerName, Language language) {
        return cpToContainer(containerName,
                localCodeFilePath(containerName, language),
                containerCodeFilePath(language)
        );
    }

    public Sequence sendInputFileToContainer(String containerName) {
        return cpToContainer(containerName, localInputFilePath(containerName), containerInputFilePath());
    }

    public Sequence cpFromContainer(String containerName, String containerPath, String hostPath) {
        return new Sequence(Delimiter.SINGLE_SPACE)
                .$("docker", "cp")
                .$(containerName + ":" + containerPath, hostPath);
    }

    public Sequence retrieveOutputFile(String containerName) {
        return cpFromContainer(containerName, containerOutputFilePath(), localOutputFilePath(containerName));
    }



    // ------ config ------
    public boolean isKeepTrace() {
        return keepTrace;
    }
    public String localTempDir(String containerName) {
        return baseDir + containerName + "/";
    }
    public String codeFileName(Language language) {
        return switch (language) {
            case CPP -> "main.cpp";
            case JAVA -> "Main.java";
            case PYTHON -> "main.py";
            default -> throw new IllegalArgumentException(language.name());
        };
    }

    public String localInputFilePath(String containerName) {
        return localTempDir(containerName) + "input.txt";
    }
    public String localCodeFilePath(String containerName, Language language) {
        return localTempDir(containerName) + codeFileName(language);
    }
    public String localOutputFilePath(String containerName) {
        return localTempDir(containerName) + "output.txt";
    }

    public String containerInputFilePath() {
        return containerDir + "input.txt";
    }
    public String containerCodeFilePath(Language language) {
        return containerDir + codeFileName(language);
    }
    public String containerOutputFilePath() {
        return containerDir + "output.txt";
    }
    // ------ config end ------


    private Sequence compileCommand(Language language) {
        return switch (language) {
            case CPP -> new Sequence(Delimiter.SINGLE_SPACE)
                    .$("g++", codeFileName(language), "-o", "main.out");
            case JAVA -> new Sequence(Delimiter.SINGLE_SPACE)
                    .$( "javac", codeFileName(language));
            default -> throw new IllegalArgumentException(language.name());
        };
    }

    private Sequence judgeCommand(Language language, int maxTimeMillis, int maxMemory) {
        Sequence sequence = new Sequence(Delimiter.SINGLE_SPACE)
                .$("ulimit", "-t", (int)Math.ceil(maxTimeMillis / 1000.0) , "&&")
                .$("ulimit", "-v", maxMemory, "&&");

        switch (language) {
            case CPP -> sequence.$("./main.out");
            case JAVA -> sequence.$("java", "Main");
            case PYTHON -> sequence.$("python", "main.py");
            default -> throw new IllegalArgumentException(language.name());
        }

        sequence.$("<", containerInputFilePath(), ">", containerOutputFilePath());
        return sequence;
    }
}
