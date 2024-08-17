package top.cary61.carycode.api.controller;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.entity.enumeration.Authority;
import top.cary61.carycode.api.entity.enumeration.Difficulty;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;
import top.cary61.carycode.api.entity.po.Problem;
import top.cary61.carycode.api.entity.po.ProblemTag;
import top.cary61.carycode.api.entity.po.UserInfo;
import top.cary61.carycode.api.entity.vo.ProblemVO;
import top.cary61.carycode.api.exception.AuthorizationException;
import top.cary61.carycode.api.exception.ParameterException;
import top.cary61.carycode.api.exception.PersistenceException;
import top.cary61.carycode.api.exception.ServiceException;
import top.cary61.carycode.api.mapper.ProblemCaseMapper;
import top.cary61.carycode.api.mapper.ProblemMapper;
import top.cary61.carycode.api.mapper.ProblemTagMapper;
import top.cary61.carycode.api.service.ProblemService;
import top.cary61.carycode.commons.entity.Result;
import top.cary61.carycode.commons.entity.enums.Storage;
import top.cary61.carycode.commons.entity.po.ProblemCase;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Value("${problem.page-size}")
    private int pageSize;

    @Resource
    private Context context;

    @Resource
    private ProblemMapper problemMapper;

    @Resource
    private ProblemTagMapper problemTagMapper;

    @Resource
    private ProblemCaseMapper problemCaseMapper;

    @Resource
    private ProblemService problemService;

    @GetMapping
    public Result<ProblemVO> getProblem(Long id) {
        if (id == null) {
            throw new ParameterException();
        }


        Problem problem = problemMapper.selectById(id);
        if (problem == null) {
            throw new ServiceException("题目不存在");
        }
        List<String> problemTagList = problemTagMapper.getAll(id)
                .stream()
                .map(ProblemTag::getName)
                .toList();

        return Result.ok(
                problemService.fillVOWithSubmissionState(
                        ProblemVO.builder()
                            .problem(problem)
                            .tags(problemTagList)
                            .build()
                )
        );
    }

    @PostMapping
    public Result<?> addProblem(@RequestBody ProblemDTO problemDTO) {
        Problem problem = problemDTO.getProblem();
        List<String> tags = problemDTO.getTags();
        boolean b = problemService.addProblem(problem, tags);
        if (!b) {
            throw new PersistenceException("上传题目失败");
        }
        return Result.ok();
    }

    @PutMapping
    public Result<?> updateProblem(@RequestBody ProblemDTO problemDTO) {
        Problem problem = problemDTO.getProblem();
        List<String> tags = problemDTO.getTags();
        UserInfo userInfo = context.getUserInfo();
        long uploaderUserId = problemMapper.selectById(problem.getId()).getUploaderUserId();
        if (userInfo.getId() != uploaderUserId && Objects.equals(userInfo.getAuthority(), Authority.USER)) {
            throw new AuthorizationException("没有权限修改别人的题目");
        }
        problem.setUploaderUserId(uploaderUserId);
        boolean b = problemService.updateProblem(problem, tags);
        if (!b) {
            throw new PersistenceException("更新题目失败");
        }
        return Result.ok();
    }

    @GetMapping("/list")
    public Result<List<ProblemVO>> getProblems(Integer pageId,
                                               String difficulty,
                                               String submissionState,
                                               String tag) {
        if (pageId == null) {
            pageId = 1;
        }
        Args args = checkArgs(difficulty, submissionState, tag);
        List<ProblemVO> problems = problemService.getProblems(pageId, args.difficulty, args.submissionState, args.tag);
        if (problems == null) {
            throw new PersistenceException("请求题单失败！");
        }
        return Result.ok(problems);
    }

    @GetMapping("/list/pages-total")
    public Result<Long> getPagesTotal(String difficulty,
                                      String submissionState,
                                      String tag) {
        Args args = checkArgs(difficulty, submissionState, tag);
        long totalCount = problemMapper.getCount(args.difficulty, args.submissionState, args.tag, context.getUserId());
        return Result.ok((totalCount - 1) / pageSize + 1);
    }

    @GetMapping("/title")
    public Result<String> getProblemTitle(Long id) {
        if (id == null) {
            throw new ParameterException("缺少参数！");
        }

        String title = problemMapper.getTitle(id);
        if (title == null) {
            throw new PersistenceException("请求题目标题失败");
        }
        return Result.ok(title);
    }

    @Data
    static class ProblemLimit {
        Integer maxTimeMillis;
        Integer maxMemory;
    }

    @GetMapping("/limit")
    public Result<ProblemLimit> getLimit(Long id) {
        List<ProblemCase> problemCases = problemCaseMapper.getListByProblemId(id);
        int maxTimeMillis = Integer.MAX_VALUE, maxMemory = Integer.MAX_VALUE;
        for (ProblemCase problemCase : problemCases) {
            maxTimeMillis = Math.min(maxTimeMillis, problemCase.getMaxTimeMillis());
            maxMemory = Math.min(maxMemory, problemCase.getMaxMemory());
        }

        ProblemLimit problemLimit = new ProblemLimit();
        problemLimit.maxTimeMillis = maxTimeMillis;
        problemLimit.maxMemory = maxMemory;

        return Result.ok(problemLimit);
    }

    @GetMapping("/cases")
    public Result<List<ProblemCase>> getCases(Long id) {
        List<ProblemCase> problemCases = problemCaseMapper.getListByProblemId(id);
        if (problemCases == null) {
            throw new ServiceException("题目不存在");
        }
        return Result.ok(problemCases);
    }

    @PutMapping("/cases")
    public Result<?> updateCases(@RequestBody List<ProblemCase> problemCases) {
        requireConsistenceAndAuth(problemCases);
        mbToB(problemCases);
        // 清除先前的样例
        problemCaseMapper.deleteBatchByProblemId(problemCases.get(0).getProblemId());
        int cnt = 0;
        LocalDateTime now = LocalDateTime.now();
        for (var problemCase : problemCases) {
            problemCase.setId(null);
            problemCase.setSerial(cnt + 1);
            problemCase.setCreateTime(now);
            cnt += problemCaseMapper.insert(problemCase);
        }
        if (cnt != problemCases.size()) {
            throw new ServiceException("更新题目样例失败");
        }
        return Result.ok();
    }

    @GetMapping("/available")
    public Result<Boolean> isAvailable(Long id) {
        return Result.ok(problemCaseMapper.getListByProblemId(id).size() > 0);
    }

    @GetMapping("/search")
    public Result<List<ProblemVO>> search(String queryString, Integer pageId) {
        if (pageId == null) {
            pageId = 1;
        }
        return Result.ok(
                problemService.search(queryString, pageId)
        );
    }

    @DeleteMapping("")
    public Result<?> delete(Long id) {
        Problem problem = problemMapper.selectById(id);
        if (problem == null) {
            throw new ServiceException("题目不存在");
        }
        if (!context.loggedInAs(problem.getUploaderUserId()) && context.getUserInfo().getAuthority() != Authority.ADMINISTRATOR) {
            throw new AuthorizationException("没有权限删除题目");
        }
        problemMapper.deleteById(id);
        return Result.ok();
    }

    private Args checkArgs(String difficulty,
                      String submissionState,
                      String tag) {
        Args args = new Args();
        args.difficulty = Difficulty.of(difficulty);
        if (context.isLoggedIn()) {
            args.submissionState = SubmissionState.of(submissionState);
        }
        if (!StringUtils.isBlank(tag)) {
            args.tag = tag;
        }
        return args;
    }

    private void requireConsistenceAndAuth(List<ProblemCase> problemCases) {
        int size = problemCases.size();
        if (size == 0) {
            throw new ParameterException("至少需要一个样例");
        }
        long problemId = problemCases.get(0).getProblemId();
        long owner = problemMapper.selectById(problemId).getUploaderUserId();
        if (!context.loggedInAs(owner) && context.getUserInfo().getAuthority() != Authority.ADMINISTRATOR) {
            throw new AuthorizationException("没有权限更改他人的题目");
        }
        for (int i = 1; i < size; i++) {
            if (problemCases.get(i).getProblemId() != problemId) {
                throw new ParameterException("参数无效！所有样例必须属于同一个题目！");
            }
        }
    }

    private void mbToB(List<ProblemCase> problemCases) {
        problemCases.forEach(e -> e.setMaxMemory( e.getMaxMemory() * Storage.MB.intSize));
    }

    private static class Args {
        Difficulty difficulty;
        SubmissionState submissionState;

        String tag;
    }


    @Data
    static class ProblemDTO {

        Problem problem;

        List<String> tags;
    }

}

