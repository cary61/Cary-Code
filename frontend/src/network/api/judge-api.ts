import type {JudgeRequest} from "@/types/JudgeRequest";
import api from "@/network/http/api";
import type {PollingMessage} from "@/types/PollingMessage";
import type {SubmissionLog} from "@/types/po/SubmissionLog";

const url = '/judge';

export async function judge(judgeRequest: JudgeRequest): Promise<PollingMessage<SubmissionLog>> {
    const resp = await api.post(url, judgeRequest);
    return resp.data as PollingMessage<SubmissionLog>;
}

export async function getResult(uuid: string): Promise<PollingMessage<SubmissionLog>> {
    const resp = await api.get(`${url}/result`, { params: {uuid} });
    return resp.data as PollingMessage<SubmissionLog>;
}
