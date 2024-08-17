import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/test',
    component: () => import('@/views/Test.vue'),
    meta: {
        title: '测试'
    }
}

export default route;