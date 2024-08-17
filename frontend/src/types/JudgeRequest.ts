import type {Language} from "@/types/enum/Language";

export interface JudgeRequest {

    userId: number;

    problemId: number;

    language: string;

    code: Language;
}