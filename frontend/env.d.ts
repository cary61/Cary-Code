/// <reference types="vite/client" />

// 环境变量类型 - vite
interface ImportMetaEnv {
    readonly ENV_API_SOCKET: string;
    readonly ENV_OSS_SOCKET: string;
    readonly ENV_TITLE_SUFFIX: string;
}
  
interface ImportMeta {
    readonly env: ImportMetaEnv
}


// 配合component类型的类型 - arco.design
declare module '*.vue' {
    import { DefineComponent } from 'vue';
    // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
    const component: DefineComponent<{}, {}, any>;
    export default component;
}