import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/forum',
    component: () => import('@/views/forum/Forum.vue'),
    meta: {
        title: '讨论'
    }
}

export default route;