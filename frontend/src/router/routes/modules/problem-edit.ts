import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/problem-edit/:problemId?',
    component: () => import('@/views/problem-edit/ProblemEdit.vue'),
    meta: {
        title: '题目编辑'
    }
}

export default route;