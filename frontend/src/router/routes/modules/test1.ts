import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/test1',
    component: () => import('@/views/Test1.vue'),
    meta: {
        title: '测试1'
    }
}

export default route;