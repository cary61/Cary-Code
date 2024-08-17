package top.cary61.carycode.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;
import top.cary61.carycode.api.mapper.SubmissionCaseLogMapper;
import top.cary61.carycode.api.mapper.SubmissionCodeMapper;
import top.cary61.carycode.api.mapper.SubmissionLogMapper;
import top.cary61.carycode.api.service.SubmissionService;
import top.cary61.carycode.commons.entity.Result;
import top.cary61.carycode.commons.entity.enums.JudgeStatus;
import top.cary61.carycode.commons.entity.po.SubmissionCaseLog;
import top.cary61.carycode.commons.entity.po.SubmissionCode;
import top.cary61.carycode.commons.entity.po.SubmissionLog;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Resource
    private Context context;
    @Resource
    private SubmissionService submissionService;
    @Resource
    private SubmissionLogMapper submissionLogMapper;
    @Resource
    private SubmissionCaseLogMapper submissionCaseLogMapper;
    @Resource
    private SubmissionCodeMapper submissionCodeMapper;


    @GetMapping("/state")
    public Result<SubmissionState> getState(Long userId, Long problemId) {
        return Result.ok(submissionService.getState(userId, problemId));
    }

    @GetMapping("/by-problem-id")
    public Result<List<SubmissionLog>> getListByProblemId(Long problemId) {
        return Result.ok(submissionLogMapper.getListByUserIdAndProblemId(context.getUserId(), problemId));
    }

    @GetMapping("/cases")
    public Result<List<SubmissionCaseLog>> getSubmissionCaseLogs(String uuid) {
        return Result.ok(submissionCaseLogMapper.getListByUuid(uuid));
    }

    @GetMapping("/code")
    public Result<SubmissionCode> getCode(String uuid) {
        return Result.ok(submissionCodeMapper.getByUuid(uuid));
    }
}
