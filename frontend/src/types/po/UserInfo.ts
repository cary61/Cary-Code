import type { Authority } from "../enum/Authority";

export default interface UserInfo {
    id: number;
    name: string;
    nickname: string;
    avatar: string;
    signature: string;
    authority: Authority;
}