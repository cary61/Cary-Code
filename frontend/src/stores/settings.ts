import { ref, computed, reactive, readonly } from 'vue';
import { defineStore } from 'pinia';

export const useSettingsStore = defineStore('settings', () => {
    
    // 登录表单是否展示
    const loginFormVisible = ref(false);
    const setLoginFormVisible = (newValue: boolean) => loginFormVisible.value = newValue;

    return {
        loginFormVisible: readonly(loginFormVisible), setLoginFormVisible
    };
});