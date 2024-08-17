import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/submission-log/:uuid',
    name: 'submission-log',
    component: () => import('@/views/submission-log/SubmissionLog.vue'),
    meta: {
        title: '提交记录'
    }
}

export default route;