import type { JudgeStatus } from "@/types/enum/JudgeStatus";

export interface SubmissionLog {
    id: number;
    uuid: string;
    userId: number;
    problemId: number;
    judgeStatus: JudgeStatus;
    timeMillis: number;
    memory: number;
    createTime: Date;
}