import { ref, computed, reactive, readonly } from 'vue';
import { defineStore } from 'pinia';

import { useLocalStorage } from '@vueuse/core';
import type UserInfo from '@/types/po/UserInfo';
import { getUserInfo } from '@/network/api/auth-api';

export const useAuthStore = defineStore('auth', () => {
    
    // JWT Token
    const token = useLocalStorage('token', '');
    const setToken = (newToken: string) => token.value = newToken;

    const userInfoInit: UserInfo = {
        id: 0,
        name: '',
        nickname: '',
        avatar: '',
        signature: '',
        authority: 'UNLOGGEDIN'
    }

    // 当前登录用户的信息
    const userInfo: UserInfo = reactive({...userInfoInit});
    const setUserInfo = (newUserInfo: UserInfo) => Object.assign(userInfo, newUserInfo);
    
    // 用户是否已登录
    const isLoggedIn = computed(() => userInfo.id != 0);
    

    // 重置 auth
    const reset = () => {
        setToken('');
        setUserInfo(userInfoInit);
    };

    if (token.value != '') {
        getUserInfo()
            .then(data => setUserInfo(data))
            .catch(() => reset());
    }

    return {
        token: readonly(token), setToken,
        userInfo: readonly(userInfo), setUserInfo,
        isLoggedIn,
        reset
    };
});