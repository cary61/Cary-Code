import type { Difficulty } from "../enum/Difficulty";
import type { ProblemState } from "../enum/ProblemStatus";

export default interface Problem {
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
}