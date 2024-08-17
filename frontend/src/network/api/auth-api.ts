import api from "@/network/http/api";
import { useAuthStore } from "@/stores/auth";
import { useSettingsStore } from "@/stores/settings";
import type UserInfo from "@/types/po/UserInfo";
import { Message } from "@arco-design/web-vue";


export function register(authDTO: AuthDTO) {
    return api.post('/auth/register', authDTO)
        .then(() => Message.success("注册成功！"));
}

export function login(authDTO: AuthDTO) {
    const auth = useAuthStore();
    return api.post('/auth/login', authDTO)
        .then(resp => resp.data as AuthVO)
        .then(data => {
            auth.setToken(data.token);
            auth.setUserInfo(data.userInfo);
            Message.success('登录成功！');
        });
}

export function logout() {
    const auth = useAuthStore();
    return api.post('/auth/logout')
        .then(() => Message.success('账号退出成功！'))
        .finally(() => auth.reset());
}

export function updatePassword(updatePasswordDTO: UpdatePasswordDTO) {
    const settings = useSettingsStore();
    api.put('/auth/password', updatePasswordDTO)
        .then(() => Message.success('密码修改成功！请重新登录'))
        .then(() => settings.setLoginFormVisible(true));
}

export async function getUserInfo(): Promise<UserInfo> {
    const resp = await api.get('/auth/user-info');
    return resp.data;
}

export interface AuthDTO {
    username: string;
    password: string;
}

export interface AuthVO {
    userInfo: UserInfo;
    token: string;
}

export interface UpdatePasswordDTO {
    oldPassword: string;
    newPassword: string;
}