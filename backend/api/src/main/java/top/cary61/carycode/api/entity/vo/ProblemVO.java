package top.cary61.carycode.api.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import top.cary61.carycode.api.entity.enumeration.SubmissionState;
import top.cary61.carycode.api.entity.po.Problem;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProblemVO {

    private Problem problem;

    private List<String> tags;

    private SubmissionState submissionState;
}