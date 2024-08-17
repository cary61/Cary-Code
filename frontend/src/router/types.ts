// 路由meta类型 - router.vuejs.org

import 'vue-router'

// 为了确保这个文件被当作一个模块，添加至少一个 `export` 声明
export { }

declare module 'vue-router' {
    interface RouteMeta {

        requiresAdmin?: boolean;

        requiresAuth?: boolean;

        title?: string;
    }
}



// 路由记录类型 - arco.design
import { defineComponent } from 'vue';
import type { RouteMeta, NavigationGuard } from 'vue-router';

export type Component<T = any> =
        | ReturnType<typeof defineComponent>
        | (() => Promise<typeof import('./modules*.vue')>)
        | (() => Promise<T>);

export interface AppRouteRecordRaw {
    path: string;
    name?: string | symbol;
    meta?: RouteMeta;
    redirect?: string;
    component?: Component | string;
    children?: AppRouteRecordRaw[];
    alias?: string | string[];
    props?: Record<string, any>;
    beforeEnter?: NavigationGuard | NavigationGuard[];
    fullPath?: string;
}

