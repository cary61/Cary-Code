import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/problem-list/:pageId?',
    component: () => import('@/views/problem-list/ProblemList.vue'),
    meta: {
        title: '题单'
    }
}

export default route;