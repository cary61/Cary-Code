import type { JudgeStatus } from "@/types/enum/JudgeStatus";
import type {Language} from "@/types/enum/Language";

export interface SubmissionCode {
    id: number;
    submissionUuid: string;
    language: Language;
    code: string;
}