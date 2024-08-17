import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/problem-cases/:problemId?',
    component: () => import('@/views/problem-cases/ProblemCases.vue'),
    meta: {
        title: '管理样例'
    }
}

export default route;