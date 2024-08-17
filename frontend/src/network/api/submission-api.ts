import api from "@/network/http/api";
import type { SubmissionState } from "@/types/enum/SubmissionState";
import type { SubmissionLog } from "@/types/po/SubmissionLog";
import type {SubmissionCaseLog} from "@/types/po/SubmissonCaseLog";
import type {SubmissionCode} from "@/types/po/SubmissionCode";

const url = '/submission';

export async function getSubmissionState(userId: number, problemId: number) {
    const resp = await api.get(`${url}/state`, { params: { userId, problemId } });
    return resp.data as SubmissionState;
}

export async function getSubmissionListByProblemId(problemId: number) {
    const resp = await api.get(`${url}/by-problem-id`, { params: { problemId } });
    return resp.data as SubmissionLog[];
}

export async function getSubmissionCaseLogs(uuid: string) {
    const resp = await api.get(`${url}/cases`, { params: { uuid } });
    return resp.data as SubmissionCaseLog[];
}

export async function getSubmissionCode(uuid: string) {
    const resp = await api.get(`${url}/code`, { params: { uuid } });
    return resp.data as SubmissionCode;
}