package top.cary61.carycode.judge.node.function;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class DockerScanner {

    private final ThreadLocal<ScanThread> scanThreadLocal = new ThreadLocal<>();

    void startScan(String containerName) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "stats", containerName
        );
        Process process = pb.start();
        scanThreadLocal.set(new ScanThread(process));
    }

    long getMemoryUsed() {
        if (scanThreadLocal.get() == null) {
            return -1;
        }
        long ret = scanThreadLocal.get().getMemoryUsed();
        scanThreadLocal.remove();
        return ret;
    }

    public static long parseMemory(String line) {
        String[] words = line.split("\\s+");
        System.out.println(line);
        String memoryInfo = words[3];
        if (Objects.equals(memoryInfo, "--")) {
            return -1;
        }
        int len = memoryInfo.length();
        StringBuilder numberBuilder = new StringBuilder();
        StringBuilder unitBuilder = new StringBuilder();
        int index = 0;
        while (index < len) {
            char ch = memoryInfo.charAt(index);
            if (Character.isLetter(ch)) {
                break;
            }
            numberBuilder.append(ch);
            index++;
        }
        while (index < len) {
            char ch = memoryInfo.charAt(index);
            unitBuilder.append(ch);
            index++;
        }
        double number = Double.parseDouble(numberBuilder.toString());
        long unitValue = unitToValue(unitBuilder.toString());
        return (long)(number * unitValue);
    }

    private static long unitToValue(String unit) {
        return switch (unit) {
            case "B" -> 1;
            case "KiB" -> 1024;
            case "MiB" -> 1024 * 1024;
            case "GiB" -> 1024 * 1024 * 1024;
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
    }

}
