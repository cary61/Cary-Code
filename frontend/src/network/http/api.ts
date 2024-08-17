import axios from 'axios';
import qs from 'qs';
import { useAuthStore } from '@/stores/auth';
import { useSettingsStore } from '@/stores/settings';
import type Result from '@/types/Result';
import { Message } from '@arco-design/web-vue';

const apiSocket = import.meta.env.ENV_API_SOCKET;

const api = axios.create({
    baseURL: apiSocket,
    paramsSerializer: (params) => qs.stringify(params, {arrayFormat: 'repeat'})
});


api.interceptors.request.use(
    config => {
        // 通过状态管理获取 token 并放入请求头
        const auth = useAuthStore();
        config.headers.Authorization = `Bearer ${auth.token}`;
        return config;
    },
    error => {
        Message.error("请求有误");
        Promise.reject(error);
    }
);

api.interceptors.response.use(
    response => {
        let result = response.data as Result<any>;
        if (!result.ok) {
            let msg = result.msg;
            if (msg == null) {
                msg = '后台未指定错误'
            }
            Message.error(msg);
            throw result.msg;
        }
        response.data = result.data;
        return response;
    },
    error => {
        if (error.response?.status == 401) {
            Message.info("请先登录！");
            const settings = useSettingsStore();
            settings.setLoginFormVisible(true);
            const auth = useAuthStore();
            auth.reset();
            return Promise.reject(error);
        }
        Message.error('请求失败，请检查网络是否已连接！' + error);
        return Promise.reject(error);
    }
)

export default api;