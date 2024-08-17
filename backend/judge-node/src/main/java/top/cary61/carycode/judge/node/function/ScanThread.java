package top.cary61.carycode.judge.node.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScanThread extends Thread {

    private final Process process;
    private final List<Long> memories;

    private volatile boolean working;

    ScanThread(Process process) {
        this.process = process;
        this.memories = new ArrayList<>();
        this.working = true;
        this.start();
    }

    @SuppressWarnings("deprecation")
    public long getMemoryUsed() {
//        this.interrupt();
//        this.stop();
        working = false;
        System.out.println(memories);
        process.destroyForcibly();
        if (memories.size() == 0) {
//            throw new JudgeException(JudgeStatus.SE, "监测内存使用失败");
            return -1;
        }
        return memories.stream()
                .mapToLong(Long::longValue)
                .max()
                .getAsLong();
    }

    @Override
    public void run() {
        try (BufferedReader reader = Optional.of(process.getInputStream())
                .map(InputStreamReader::new)
                .map(BufferedReader::new)
                .get()) {
            int cnt = 0;
            String line;
            while (working && (line = reader.readLine()) != null) {
                cnt++;
                if (cnt % 2 == 1) {
                    continue;
                }
                long memory = DockerScanner.parseMemory(line);
                if (memory != -1) {
                    memories.add(memory);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
