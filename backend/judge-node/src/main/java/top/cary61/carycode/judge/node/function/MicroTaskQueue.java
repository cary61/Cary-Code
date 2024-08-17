package top.cary61.carycode.judge.node.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary61.carycode.commons.entity.MicroTask;
import top.cary61.carycode.commons.entity.MicroTaskResult;
import top.cary61.carycode.judge.node.remote.JudgeServerRemote;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MicroTaskQueue {

    @Resource
    private Judger judger;

    @Resource
    private JudgeServerRemote judgeServerRemote;

    private final BlockingQueue<MicroTask> queue;
    private final ExecutorService executorService;



    public void push(MicroTask microTask) {
        try {
            queue.put(microTask);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public MicroTaskQueue(@Value("${judge.thread-amount}") int threadAmount) {
        this.queue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newFixedThreadPool(threadAmount);
        startJudging();
    }


    public void startJudging() {
        new Thread(() -> {
            while (true) {
                try {
                    // 从队列中取出判题任务
                    MicroTask microTask = queue.take();
                    // 使用线程池执行判题任务
                    executorService.execute(() -> {
                        // 执行判题任务的逻辑
                        MicroTaskResult microTaskResult = judger.judge(microTask);
                        System.out.println(microTaskResult);
                        judgeServerRemote.accomplish(microTaskResult);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
