import type UserInfo from "@/types/po/UserInfo";
import api from "../http/api";

const url = 'user';

export async function getUser(id: number | string): Promise<UserInfo> {
    const resp = await api.get(url, { params: {id} });
    return resp.data as UserInfo;
}

export async function updateUser(userInfo: UserInfo): Promise<null> {
    const resp = await api.put(url, userInfo);
    return resp.data;
}