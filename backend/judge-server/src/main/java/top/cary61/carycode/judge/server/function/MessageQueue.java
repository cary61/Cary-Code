package top.cary61.carycode.judge.server.function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.cary61.carycode.commons.entity.JudgeRequest;
import top.cary61.carycode.commons.entity.MicroTask;
import top.cary61.carycode.commons.entity.PollingMessage;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;
import top.cary61.carycode.commons.entity.po.ProblemCase;
import top.cary61.carycode.commons.entity.po.SubmissionCaseLog;
import top.cary61.carycode.commons.entity.po.SubmissionCode;
import top.cary61.carycode.commons.entity.po.SubmissionLog;
import top.cary61.carycode.judge.server.entity.Task;
import top.cary61.carycode.judge.server.mapper.ProblemCaseMapper;
import top.cary61.carycode.judge.server.mapper.SubmissionCaseLogMapper;
import top.cary61.carycode.judge.server.mapper.SubmissionLogMapper;
import top.cary61.carycode.judge.server.remote.JudgeNodeRemote;
import top.cary61.carycode.judge.server.util.Util;
import top.cary61.carycode.judge.server.mapper.SubmissionCodeMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class MessageQueue {

    @Value("${judge.default.time-interval-millis}")
    private long DEFAULT_TIME_INTERVAL_MILLIS;

    @Resource
    private ProblemCaseMapper problemCaseMapper;
    @Resource
    private SubmissionLogMapper submissionLogMapper;
    @Resource
    private SubmissionCodeMapper submissionCodeMapper;
    @Resource
    private SubmissionCaseLogMapper submissionCaseLogMapper;

    @Resource(type = JudgeNodeRemote.class)
    private JudgeNodeRemote judgeNodeRemote;



    private final Object lock = new Object();
    @Value("${judge.default.available-nodes-count}")
    volatile int availableNodesCount;
    private final Map<String, Task> uuidToTask = new HashMap<>();
    private final Map<String, List<SubmissionCaseLog>> uuidToCaseLogs = new HashMap<>();
    private final Queue<MicroTask> microTaskQueue = new LinkedList<>();



    public PollingMessage<SubmissionLog> push(JudgeRequest judgeRequest) {
        // 查出所有case，并且设立任务
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        List<ProblemCase> problemCases = problemCaseMapper.getAllByProblemId(judgeRequest.getProblemId());
        Task task = new Task(judgeRequest.getUserId(),
                judgeRequest.getProblemId(),
                problemCases.size(),
                judgeRequest.getLanguage(),
                judgeRequest.getCode(),
                now
        );
        List<SubmissionCaseLog> caseLogs = new ArrayList<>(problemCases.size());
        synchronized (lock) {
            uuidToTask.put(uuid, task);
            uuidToCaseLogs.put(uuid, caseLogs);
        }

        // 包装微任务
        synchronized (lock) {
            for (ProblemCase problemCase : problemCases) {
                MicroTask microTask = MicroTask.builder()
                        .uuid(uuid)
                        .problemCase(problemCase)
                        .language(judgeRequest.getLanguage())
                        .code(judgeRequest.getCode())
                        .build();

                microTaskQueue.offer(microTask);
    //            pushMicroTasksIfPossible();
            }
            lock.notifyAll();
        }
        return new PollingMessage<>(
                uuid,
                false,
                DEFAULT_TIME_INTERVAL_MILLIS * problemCases.size(),
                null
        );
    }

    public void accomplish(String uuid,
                           int caseSerial,
                           JudgeStatus judgeStatus,
                           Integer timeMillis,
                           Integer memory) {
        synchronized (lock) {
            lock.notifyAll();
            availableNodesCount++;
        }
//        pushMicroTasksIfPossible();

        SubmissionCaseLog submissionCaseLog = SubmissionCaseLog.builder()
                .submissionUuid(uuid)
                .caseSerial(caseSerial)
                .judgeStatus(judgeStatus)
                .timeMillis(timeMillis)
                .memory(memory)
                .build();
        submissionCaseLogMapper.insert(submissionCaseLog);


        boolean allDone;  // 所有 micro task 全部完成 的标志
        Task task;
        List<SubmissionCaseLog> submissionCaseLogs;

        synchronized (lock) {
            task = uuidToTask.get(uuid);
            submissionCaseLogs = uuidToCaseLogs.get(uuid);
            submissionCaseLogs.add(submissionCaseLog);
            allDone = submissionCaseLogs.size() == task.getCasesAmount();
        }

        if (allDone) {
            SubmissionLog submissionLog = Util.merge(
                    submissionCaseLogs,
                    task.getProblemId(),
                    task.getUserId(),
                    task.getCreateTime()
            );
            submissionLogMapper.insert(submissionLog);
            submissionCodeMapper.insert(new SubmissionCode(
                    null,
                    uuid,
                    task.getLanguage(),
                    task.getCode()
            ));
            synchronized (lock) {
                lock.notifyAll();
                uuidToTask.remove(uuid);
                uuidToCaseLogs.remove(uuid);
            }
        }
    }

    public PollingMessage<SubmissionLog> getRestTime(String uuid) {
//        pushMicroTasksIfPossible();
        synchronized (lock) {
            lock.notifyAll();
            if (uuidToTask.get(uuid) == null) {
                return null;
            }
        }
        return new PollingMessage<>(uuid, false, DEFAULT_TIME_INTERVAL_MILLIS, null);
    }

//    private void pushMicroTasksIfPossible() {
//        while (true) {
//            MicroTask microTask;
//            synchronized (lock) {
//                if (availableNodesCount > 0 && !microTaskQueue.isEmpty()) {
//                    microTask = microTaskQueue.poll();
//                    availableNodesCount--;
//                } else {
//                    break;
//                }
//            }
//            judgeNodeRemote.judgeMicroTask(microTask);
//        }
//    }

    @PostConstruct
    public void startConsumerThread() {
        new Thread(() -> {
            while (true) {
                MicroTask microTask;
                synchronized (lock) {
                    while (availableNodesCount == 0 || microTaskQueue.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    microTask = microTaskQueue.poll();
                    availableNodesCount--;
                }
                judgeNodeRemote.judgeMicroTask(microTask);
            }
        }).start();
    }

}

