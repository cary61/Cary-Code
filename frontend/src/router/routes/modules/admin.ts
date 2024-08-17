import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/admin',
    component: () => import('@/views/admin/Admin.vue'),
    meta: {
        title: '管理'
    }
}

export default route;