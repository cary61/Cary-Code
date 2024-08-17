import type {JudgeRequest} from "@/types/JudgeRequest";
import api from "@/network/http/api";
import type {PollingMessage} from "@/types/PollingMessage";
import type {SubmissionLog} from "@/types/po/SubmissionLog";
import type UserDO from "@/types/do/UserDO";

const url = '/admin';

export async function searchUser(queryString: string)  {
    const resp = await api.get(`${url}/user`, { params: { queryString } });
    return resp.data as UserDO[];
}
