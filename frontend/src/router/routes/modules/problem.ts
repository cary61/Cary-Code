import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/problem',
    redirect: '/problem-list',
    children: [
        {
            path: ':problemId',
            name: 'problem',
            component: () => import('@/views/problem/Problem.vue'),
            meta: {
                title: 'ASYNC'
            }
        }
    ]
}

export default route;