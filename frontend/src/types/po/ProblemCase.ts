import type { Difficulty } from "../enum/Difficulty";
import type { ProblemState } from "../enum/ProblemStatus";

export default interface ProblemCase {
    id?: number;
    problemId: number;
    serial?: number;
    input: string;
    output: string;
    maxTimeMillis: number;
    maxMemory: number;
    createTime?: Date
}