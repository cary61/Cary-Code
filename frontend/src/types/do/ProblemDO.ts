import type { Difficulty } from "../enum/Difficulty";
import type { ProblemState } from "../enum/ProblemStatus";
import type {SubmissionState} from "@/types/enum/SubmissionState";

export default interface ProblemDO {
    id: number;
    number: string;
    title: string;
    uploaderUserId: number;
    background: string;
    description: string;
    inputFormat: string;
    outputFormat: string;
    dataRange: string;
    difficulty: Difficulty;
    state: ProblemState;
    tags: string[];
    submissionState?: SubmissionState;
}