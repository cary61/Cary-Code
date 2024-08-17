import type { AppRouteRecordRaw } from '@/router/types';

const route: AppRouteRecordRaw = {
    path: '/submission-log-list/:problemId',
    name: 'submission-log-list',
    component: () => import('@/views/submission-log-list/SubmissionLogList.vue'),
    meta: {
        title: '提交历史'
    }
}

export default route;