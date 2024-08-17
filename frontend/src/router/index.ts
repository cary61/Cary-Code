import { createRouter, createWebHashHistory, type RouteLocationNormalized } from 'vue-router';

import { useAuthStore } from '@/stores/auth';
import { useSettingsStore } from '@/stores/settings';
import { appRoutes } from './routes';
import { useTitle } from '@vueuse/core';
import api from '@/network/http/api';

const titleSuffix = import.meta.env.ENV_TITLE_SUFFIX;

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            redirect: 'problem-list'
        },
        ...appRoutes
    ],
    scrollBehavior() {
        return { top: 0 };
    }
})

// 拦截未登录行为
router.beforeEach((to) => {
    const auth = useAuthStore();
    const settings = useSettingsStore();
    if (to.meta.requiresAuth && !auth.isLoggedIn) {
        settings.setLoginFormVisible(true);
        return false;
    }
});

// 修改页面标题
router.afterEach(async (to) => {
    const _title = to.meta.title;
    let title: string;
    if (_title == 'ASYNC') {
        title = await getTitle(to);
    } else if (_title != null) {
        title = _title;
    } else {
        console.log('当前页面未设置标题');
        return;
    }
    useTitle(`${title} - ${titleSuffix}`);
});

const getTitle = async (to: RouteLocationNormalized) => {
    if (to.name == 'problem') {
        const resp = await api.get(`/problem/title?id=${to.params.problemId}`);
        return  resp.data;
    }
}

export default router
