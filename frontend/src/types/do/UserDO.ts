import type { Authority } from "../enum/Authority";

export default interface UserDO {
    id: number;
    name: string;
    nickname: string;
    signature: string;
    authority: Authority;
    registerTime: Date;
    bannedUntil?: Date;
    banCount: number;
}