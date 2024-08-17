package top.cary61.carycode.api.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import top.cary61.carycode.api.entity.enumeration.Difficulty;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;

import javax.annotation.Resource;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProblemMapperTest {

    @Value("${problem.page-size}")
    private int pageSize;

    @Resource
    private ProblemMapper problemMapper;

    @Test
    public void getList() {
        problemMapper.getList(1, pageSize, null, null, null, null);
    }

    @Test
    public void getListWithDifficulty() {
        problemMapper.getList(1, pageSize, Difficulty.EASY, null, null, null);
    }

    @Test
    public void getListWithSubmissionStateUnattempted() {
        problemMapper.getList(1, pageSize, null, SubmissionState.UNATTEMPTED, null, 1L);
    }

    @Test
    public void getListWithSubmissionStateSolved() {
        problemMapper.getList(1, pageSize, null, SubmissionState.SOLVED, null, 1L);
    }

    @Test
    public void getListWithSubmissionStateAttempted() {
        problemMapper.getList(1, pageSize, null, SubmissionState.ATTEMPTED, null, 1L);
    }

    @Test
    public void getListWithTag() {
        problemMapper.getList(1, pageSize,null, null, "字符串", null);
    }

    @Test
    public void getListWithAllArgs() {
        problemMapper.getList(1, pageSize, Difficulty.EASY, SubmissionState.SOLVED, "字符串", 1L);
    }

}
