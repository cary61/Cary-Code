import type Problem from "@/types/po/Problem";
import api from "../http/api";
import type ProblemDO from "@/types/do/ProblemDO";
import type { SubmissionState } from "@/types/enum/SubmissionState";
import type { Difficulty } from "@/types/enum/Difficulty";
import type ProblemCase from "@/types/po/ProblemCase";

const url = '/problem';

export async function getProblem(id: string | number): Promise<ProblemDO> {
    const resp = await api.get(url, { params: { id } });
    const problemVO = resp.data as ProblemVO;
    return parseDO(problemVO);
}

export async function addProblem(problemDO: ProblemDO): Promise<null> {
    const resp = await api.post(url, parseDTO(problemDO));
    return resp.data;
}

export async function updateProblem(problemDO: ProblemDO): Promise<null> {
    const resp = await api.put(url, parseDTO(problemDO));
    return resp.data;
}

export async function getProblems(getProblemsDTO: GetProblemsDTO): Promise<ProblemDO[]> {
    const resp = await api.get(`${url}/list`, { params: getProblemsDTO });
    return parseDOs(resp.data as ProblemVO[]);
}

export async function getPagesTotal(getProblemsDTO: GetProblemsDTO): Promise<number> {
    const resp = await api.get(`${url}/list/pages-total`, { params: getProblemsDTO });
    return resp.data as number;
}

export async function getProblemLimit(id: number) {
    const resp = await api.get(`${url}/limit`, { params: { id } });
    return resp.data as ProblemLimit;
}

export async function getProblemCases(id: number) {
    const resp = await api.get(`${url}/cases`,  { params: { id } });
    return resp.data as ProblemCase[];
}

export async function updateProblemCases(problemCases: ProblemCase[]) {
    const resp = await api.put(`${url}/cases`,  problemCases);
    return resp.data as null;
}

export async function isAvailable(problemId: number) {
    const resp = await api.get(`${url}/available`,  { params: { id: problemId } });
    return resp.data as boolean;
}

export async function searchProblem(queryString: string) {
    const resp = await api.get(`${url}/search`,  { params: { queryString } });
    return parseDOs(resp.data as ProblemVO[]);
}

export async function deleteProblem(id: number) {
    const resp = await api.delete(url,  { params: { id } });
    return resp.data as null;
}

export interface GetProblemsDTO {
    pageId?: number;
    difficulty?: Difficulty;
    submissionState?: SubmissionState;
    tag?: string;
}

export interface ProblemDTO {
    problem: Problem;
    tags: string[];
}

export interface ProblemVO {
    problem: Problem,
    tags?: string[];
    submissionState?: SubmissionState;
}

export interface ProblemLimit {
    maxTimeMillis: number;
    maxMemory: number;
}




function parseDO(problemVO: ProblemVO): ProblemDO {
    const problemDO = Object.assign({}, problemVO.problem) as any;
    if (problemVO.tags != null) {
        problemDO.tags = problemVO.tags;
    }
    if (problemVO.submissionState != null) {
        problemDO.submissionState = problemVO.submissionState;
    }
    return problemDO as ProblemDO;
}

function parseDOs(problemVOs: ProblemVO[]): ProblemDO[] {
    const problemDOs: ProblemDO[] = [];
    problemVOs.forEach(e => problemDOs.push(parseDO(e)));
    return problemDOs;
}

function parseDTO(problemDO: ProblemDO): ProblemDTO {
    const tags = problemDO.tags;
    const obj = problemDO as any;
    delete obj.tags;
    const problemDTO: ProblemDTO = {
        problem: obj,
        tags: tags
    };
    return problemDTO;
}