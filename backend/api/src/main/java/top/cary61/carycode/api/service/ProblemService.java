package top.cary61.carycode.api.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.cary61.carycode.api.config.Context;
import top.cary61.carycode.api.entity.enumeration.Difficulty;
import top.cary61.carycode.api.entity.enumeration.ProblemState;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;
import top.cary61.carycode.api.entity.po.Problem;
import top.cary61.carycode.api.entity.po.ProblemTag;
import top.cary61.carycode.api.entity.vo.ProblemVO;
import top.cary61.carycode.api.mapper.ProblemMapper;
import top.cary61.carycode.api.mapper.ProblemTagMapper;
import top.cary61.carycode.api.mapper.SubmissionLogMapper;
import top.cary61.carycode.commons.entity.po.SubmissionLog;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {

    @Value("${problem.page-size}")
    private int pageSize;

    @Resource
    private Context context;

    @Resource
    private ProblemMapper problemMapper;

    @Resource
    private ProblemTagMapper problemTagMapper;

    @Resource
    private SubmissionService submissionService;

    @Transactional
    public boolean addProblem(Problem problem, List<String> tags) {
        problem.setUploaderUserId(context.getUserId());
        if (StringUtils.isBlank(problem.getNumber())) {
            problem.setNumber("P" + (getCount() + 1 + 1000));
        }
        problem.setId(null);
        boolean b1 = problemMapper.insert(problem) == 1;
        boolean b2 = insertTags(problem, tags);
        if (b1 && b2) {
            return true;
        }
        throw new RuntimeException();
    }

    @Transactional
    public boolean updateProblem(Problem problem, List<String> tags) {
        boolean b1 = problemMapper.updateById(problem) == 1;
        problemTagMapper.delete(Wrappers.<ProblemTag>query()
                .eq("problem_id", problem.getId())
        );
        boolean b2 = insertTags(problem, tags);
        if (b1 && b2) {
            return true;
        }
        throw new RuntimeException();
    }

    private boolean insertTags(Problem problem, List<String> tags) {
        if (tags == null || tags.size() == 0) {
            return true;
        }
        boolean b = true;
        for (String tag : tags) {
            b = problemTagMapper.insert(new ProblemTag(null, problem.getId(), tag)) == 1;
        }
        return b;
    }

    public List<ProblemVO> getProblems(int pageId,
                                       Difficulty difficulty,
                                       SubmissionState submissionState,
                                       String tag) {
        return fillVOWithSubmissionState(toVOWithSubmissionState(problemMapper.getList(pageId, pageSize, difficulty, submissionState, tag, context.getUserId())));
    }

    public int getCount() {
        return problemMapper.selectList(Wrappers.query()).size();
    }

    static int requiredFactor = 10;

    public List<ProblemVO> search(String queryString, int pageId) {
        List<Problem> problems = problemMapper.selectList(Wrappers.query());
//        List<Problem> problems = getPage(pageId, pageSize);
        List<ProblemVO> result = new ArrayList<>();
        for (var problem : problems) {
            int c1, c2, c3, c4;
            c1 = FuzzySearch.ratio(queryString, problem.getTitle());
            c2 = FuzzySearch.ratio(queryString, problem.getNumber());
            c3 = FuzzySearch.ratio(queryString, problem.getBackground());
            c4 = FuzzySearch.ratio(queryString, problem.getDescription());
            int maxC = Math.max(Math.max(c1, c2), Math.max(c3, c4));
            if (maxC > requiredFactor) {
                result.add(fillVOWithSubmissionState(asVO(problem)));
            }
        }
        return result;
    }

    private List<Problem> getPage(int pageId, int pageSize) {
        return problemMapper.getList(pageId, pageSize, null, null, null, null);
    }


    private ProblemVO asVO(@NotNull Problem problem) {
        return ProblemVO.builder()
                .problem(problem)
                .build();
    }

    private List<ProblemVO> toVOWithSubmissionState(@NotNull List<Problem> problems) {
        List<ProblemVO> problemVOs = new ArrayList<>(problems.size());
        for (Problem problem : problems) {
            ProblemVO problemVo = asVO(problem);
            problemVOs.add(problemVo);
        }
        return problemVOs;
    }

    public ProblemVO fillVOWithSubmissionState(ProblemVO problemVO) {
        if (!context.isLoggedIn()) {
            return problemVO;
        }
        problemVO.setSubmissionState(
                submissionService.getState(context.getUserId(), problemVO.getProblem().getId())
        );
        return problemVO;
    }

    public List<ProblemVO> fillVOWithSubmissionState(List<ProblemVO> problemVOs) {
        if (!context.isLoggedIn()) {
            return problemVOs;
        }
        int len = problemVOs.size();
        for (int i = 0; i < len; i++) {
            problemVOs.set(i, fillVOWithSubmissionState(problemVOs.get(i)));
        }
        return problemVOs;
    }
}
