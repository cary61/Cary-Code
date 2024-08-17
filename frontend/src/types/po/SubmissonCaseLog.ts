import type { JudgeStatus } from "@/types/enum/JudgeStatus";

export interface SubmissionCaseLog {
    id: number;
    submissionUuid: string;
    caseSerial: number;
    judgeStatus: JudgeStatus;
    timeMillis: number;
    memory: number;
}